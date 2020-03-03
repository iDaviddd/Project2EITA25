package server;

import server.database.DatabaseHandler;
import utility.Hasher;
import utility.Record;
import utility.User;

import java.util.Scanner;

public class MenuHandler {

    public static boolean printMenu(Scanner in, DatabaseHandler databaseHandler){
        String input = in.nextLine();
        boolean running = true;
        switch (input) {
            case "quit":
                running = false;
                break;
            case "print":
                System.out.println("All users registered in database:");
                databaseHandler.printAllUsers();
                break;
            case "printr":
                System.out.println("All records registered in database:");
                databaseHandler.printAllRecords();
                break;
            case "printl":
                System.out.println("All logs registered in database");
                databaseHandler.printAllLogs();
                break;
            case "create":
                System.out.print("Name: ");
                String name = in.nextLine();
                System.out.print("Role(Government, Doctor, Nurse or Patient): ");
                String role = in.nextLine();
                System.out.print("Personal Number(XXXXXXXX-XXXX): ");
                String personal_number = in.nextLine();
                System.out.print("Password: ");
                String salt = User.generateSecretKey(16);
                String password = Hasher.HashPassword(in.nextLine(),  salt);
                String division = "";
                if(role.equalsIgnoreCase("doctor") || role.equalsIgnoreCase("nurse")) {
                    System.out.print("Division: ");
                    division = in.nextLine();
                }
                User newuser = new User(name, role, personal_number, password, salt, division);

                databaseHandler.addUser(newuser);
                System.out.println(name + " has been added to the system. ");
                break;
            case "find":
                System.out.print("Enter what you want to search for(name, id, perosnal_number, division)");
                String col = in.nextLine();
                System.out.print("Enter the info(If you entered name, type a name):");
                String search_term = in.nextLine();
                System.out.println(databaseHandler.findUsers(col, search_term).toString());
                break;
            case "add_record":
                System.out.print("Patient personal number:");
                String patient_personal_number = in.nextLine();
                System.out.print("Doctor personal number:");
                String doctor_personal_number = in.nextLine();
                System.out.print("Nurse perosnal number:");
                String nurse_personal_number = in.nextLine();
                System.out.print("Division:");
                division = in.nextLine();
                System.out.print("Record description:");
                String record_description = in.nextLine();
                Record r = new Record(patient_personal_number, doctor_personal_number, nurse_personal_number, division, record_description);
                databaseHandler.addRecord(r);
                System.out.println("Record has been successfully added. ");
                break;
            case "del_user":
                System.out.print("Personal number:");
                personal_number = in.nextLine();
                databaseHandler.deleteUser(personal_number);
                System.out.println("User deleted from database");
                break;
            case "help":
                System.out.println("--------- HELP --------");
                System.out.println("help - print this message");
                System.out.println("quit - close the server");
                System.out.println("print - print all registered users in database");
                System.out.println("printr - prints all registered records in database");
                System.out.println("create - Create a user");
                System.out.println("find - Search the user table for a user");
                System.out.println("add_record - Add a record");
                System.out.println("del_user - Deletes a user from database");
                break;
            default:
                System.out.println("Unknown command. Type 'help' for help.");
                break;
        }
        if(!running){
            in.close();
        }
        return running;
    }
}
