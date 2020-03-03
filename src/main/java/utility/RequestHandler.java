package utility;

import client.network.NetworkHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.security.ntlm.Server;
import server.ServerMain;
import server.database.DatabaseHandler;

import java.util.HashSet;
import java.util.List;


public class RequestHandler {

    static GsonBuilder builder = new GsonBuilder();
    static Gson gson = builder.create();
    private User selectedRecordUser = null;
    private User selectedNurse = null;
    private Record selectedRecord = null;


    public void processRequest(Request request, User user, Communicator communicator) {

        String type = request.type;
        String actionType = request.actionType;
        String data = request.data;
        boolean reply = request.reply;

        if (type.equals("records") && actionType.equals("get")) {
            System.out.println(selectedRecordUser);
            List<Record> records = getRecordsOfUser(this.selectedRecordUser);

            communicator.send(new Request("records", "get", true, "start"));
            records.forEach(a -> {
                boolean send = false;
                switch (user.getRole()) {
                    case "Doctor":
                        if (a.getDoctorPersonalNumber().equals(user.getPersonalNumber()) || a.getDivision().equals(user.getDivision()))
                            send = true;
                        break;
                    case "Nurse":
                    if (a.getNursePersonalNumber().equals(user.getPersonalNumber()) || a.getDivision().equals(user.getDivision()))
                        send = true;
                    break;
                    case "Patient":
                        if (a.getPatientPersonalNumber().equals(user.getPersonalNumber()))
                            send = true;
                        break;
                    case "Government":
                        send = true;
                        break;
                    default:
                        break;
                }
                if(send) {
                    Log log = new Log(user.getPersonalNumber(), a.getRecordId(), ActionType.LIST_RECORD.toString(), user.getName() + " listed this record.");
                    ServerMain.databaseHandler.addLog(log);
                    communicator.send(new Request("records", "get", true, a.getRecordId().toString()));
                }
            });

            communicator.send(new Request("records", "get", true, null));
        }
        else if (type.equals("users") && actionType.equals("get") && data.equals("patients")) {
            // Returns a list of all patients.
            List<User> patients = getPatients();
            System.out.println(patients);
            Log log = new Log(user.getPersonalNumber(), -1, ActionType.LIST_ALL_USERS.toString(), user.getName() + " listed all users he have access to.");
            ServerMain.databaseHandler.addLog(log);
            communicator.send(new Request("users", "get", true, "start"));
            patients.forEach(a -> {

                boolean send = false;
                switch (user.getRole()) {
                    case "Doctor":
                        send = true;
                        break;
                    case "Nurse":
                        send = true;
                        break;
                    case "Patient":
                        if (a.getPersonalNumber().equals(user.getPersonalNumber()))
                            send = true;
                        break;
                    case "Government":
                        send = true;
                        break;
                    default:
                        break;
                }
                if (send) {
                    communicator.send(new Request("users", "get", true, a.getPersonalNumber()));
                }
            });
            communicator.send(new Request("users", "get", true, null));
        }
        else if (type.equals("record") && actionType.equals("get")) {
            List<User> patients = ServerMain.databaseHandler.findUsers("personal_number", selectedRecord.getPatientPersonalNumber());
            List<User> doctors = ServerMain.databaseHandler.findUsers("personal_number", selectedRecord.getDoctorPersonalNumber());
            List<User> nurses = ServerMain.databaseHandler.findUsers("personal_number", selectedRecord.getNursePersonalNumber());

            Log log = new Log(user.getPersonalNumber(), selectedRecord.getRecordId(), ActionType.READ_RECORD.toString(), user.getName() + " viewed a record.");
            ServerMain.databaseHandler.addLog(log);

            String patientName = "";
            String doctorName = "";
            String nurseName = "";

            if (patients.size() == 1 && doctors.size() == 1 && nurses.size() == 1) {
                patientName = patients.get(0).getName();
                doctorName = doctors.get(0).getName();
                nurseName = nurses.get(0).getName();
            }

            communicator.send(new Request("record", "get", true, selectedRecord.getPatientPersonalNumber()));
            communicator.send(new Request("record", "get", true, patientName));

            communicator.send(new Request("record", "get", true, selectedRecord.getDoctorPersonalNumber()));
            communicator.send(new Request("record", "get", true, doctorName));

            communicator.send(new Request("record", "get", true, selectedRecord.getNursePersonalNumber()));
            communicator.send(new Request("record", "get", true, nurseName));

            communicator.send(new Request("record", "get", true, selectedRecord.getRecord()));
        }
        else if (type.equals("record") && actionType.equals("post")){

            //if(allowed to write to record){}
            Log log = new Log(user.getPersonalNumber(), selectedRecord.getRecordId(), ActionType.MODIFY_RECORD.toString(), user.getName() + " modified a record.");
            ServerMain.databaseHandler.addLog(log);
            ServerMain.databaseHandler.updateRecord("record", data, "record_id", selectedRecord.getRecordId().toString());
        }
        else if (type.equals("record") && actionType.equals("delete")){
            //if(allowed to delete record){}
            Log log = new Log(user.getPersonalNumber(), selectedRecord.getRecordId(), ActionType.REMOVE_RECORD.toString(), user.getName() + " deleted record.");
            ServerMain.databaseHandler.addLog(log);
            ServerMain.databaseHandler.deleteRecord(selectedRecord.getRecordId());
            selectedRecord = null;
        }
        else if (type.equals("add_record") && actionType.equals("post")){
            if(user.getRole().equals("Doctor")){
                Record record = new Record(selectedRecordUser.getPersonalNumber(), user.getPersonalNumber(), selectedNurse.getPersonalNumber(), user.getDivision(), data);
                ServerMain.databaseHandler.addRecord(record);

                int record_id = ServerMain.databaseHandler.getLatestRecordId();
                Log log = new Log(user.getPersonalNumber(), record_id, ActionType.CREATE_RECORD.toString(), user.getName() + " created record with id=" + record_id + ".");
                ServerMain.databaseHandler.addLog(log);
            }
        }
        else if (type.equals("selectRecordUser") && actionType.equals("post")) {
            System.out.println("SELECTED RECORD USeR");
            List<User> users = ServerMain.databaseHandler.findUsers("personal_number", data);
            if (users.size() != 1) {
                communicator.send(new Request("selectedRecordUser", "post", true, "failed"));
            }
            selectedRecordUser = users.get(0);
            communicator.send(new Request("selectedRecordUser", "post", true, "success"));
        }
        else if (type.equals("selectRecord") && actionType.equals("post")) {
            List<Record> records = ServerMain.databaseHandler.findRecords("record_id", data);
            if (records.size() != 1) {
                communicator.send(new Request("selectRecord", "post", true, "failed"));
            }
            selectedRecord = records.get(0);
            communicator.send(new Request("selectRecord", "post", true, "success"));
        }
        else if (type.equals("selectNurse") && actionType.equals("post")) {
            List<User> nurses = ServerMain.databaseHandler.findUsers("personal_number", data);
            if (nurses.size() != 1) {
                communicator.send(new Request("selectedRecordUser", "post", true, "failed"));
            }
            selectedNurse = nurses.get(0);
            communicator.send(new Request("selectedRecordUser", "post", true, "success"));
        }

    }

    private static List<User> getPatients() {
        System.out.println("getPatients");
        return ServerMain.databaseHandler.findUsers("role", "Patient");
    }

    private static List<Record> getRecordsOfUser(User user) {
        System.out.println("Records:");
        System.out.println("User:" + user);
        List<Record> records = null;
        if (user.getRole().equals("Patient")) {
            records = ServerMain.databaseHandler.findRecords("patient_personal_number", user.getPersonalNumber());
            System.out.println(records);
        }
        return records;
    }


}
