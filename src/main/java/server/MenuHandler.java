package server;

import server.database.DatabaseHandler;
import utility.Hasher;
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
            case "create":
                System.out.print("Name: ");
                String name = in.nextLine();
                System.out.print("Role(Government, Doctor, Nurse or Patient): ");
                String role = in.nextLine();
                System.out.print("Personal Number(XXXXXXXX-XXXX): ");
                String personal_number = in.nextLine();
                System.out.print("Password: ");
                String salt = User.generateSecretKey(8);
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
            case "help":
                System.out.println("--------- HELP --------");
                System.out.println("help - print this message");
                System.out.println("quit - close the server");
                System.out.println("print - print all registered users in database");
                System.out.println("create - Create a user");
                System.out.println("find - Search the user table for a user");
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
