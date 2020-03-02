package utility;

import client.network.NetworkHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.ServerMain;
import server.database.DatabaseHandler;

import java.util.HashSet;
import java.util.List;


public class RequestHandler {

    static GsonBuilder builder = new GsonBuilder();
    static Gson gson = builder.create();
    private User selectedRecordUser = null;
    private Record selectedRecord = null;

    public void processRequest(Request request, User user, Communicator communicator) {

        String type = request.type;
        String actionType = request.actionType;
        Object data = request.data;
        boolean reply = request.reply;


        if (type.equals("records") && actionType.equals("get")) {
            System.out.println(selectedRecordUser);
            List<Record> records = getRecordsOfUser(this.selectedRecordUser);
            communicator.send(new Request("records", "get", true, "start"));
            records.forEach(a -> communicator.send(new Request("records", "get", true, a.getRecordId().toString())));
            communicator.send(new Request("records", "get", true, null));
        }
        if(type.equals("users") && actionType.equals("get") && data.equals("patients")){
            // Returns a list of all patients.
            List<User> patients = getPatients();
            System.out.println(patients);
            communicator.send(new Request("users", "get", true, "start"));
            patients.forEach(a -> communicator.send(new Request("users", "get", true, a.getPersonalNumber())));
            communicator.send(new Request("users", "get", true, null));
        }
        if(type.equals("record") && actionType.equals("get")){
            List<User> patients = ServerMain.databaseHandler.findUsers("personal_number", selectedRecord.getPatientPersonalNumber());
            List<User> doctors = ServerMain.databaseHandler.findUsers("personal_number", selectedRecord.getDoctorPersonalNumber());
            List<User> nurses = ServerMain.databaseHandler.findUsers("personal_number", selectedRecord.getNursePersonalNumber());

            String patientName = "";
            String doctorName = "";
            String nuseName = "";

            if(patients.size() == 1 && doctors.size() == 1 && nurses.size() == 1){
                patientName = patients.get(0).getName();
                doctorName = doctors.get(0).getName();
                nuseName = nurses.get(0).getName();
            }

            communicator.send(new Request("record", "get", true, selectedRecord.getPatientPersonalNumber()));
            communicator.send(new Request("record", "get", true, patientName));

            communicator.send(new Request("record", "get", true, selectedRecord.getDoctorPersonalNumber()));
            communicator.send(new Request("record", "get", true, doctorName));

            communicator.send(new Request("record", "get", true, selectedRecord.getNursePersonalNumber()));
            communicator.send(new Request("record", "get", true, nuseName));

            communicator.send(new Request("record", "get", true, selectedRecord.getRecord()));
            }

        if (type.equals("selectRecordUser") && actionType.equals("post")) {
            System.out.println("SELECTED RECORD USeR");
            List<User> users = ServerMain.databaseHandler.findUsers("personal_number", (String) data);
            if (users.size() != 1) {
                communicator.send(new Request("selectedRecordUser", "post", true, "failed"));
            }
            selectedRecordUser = users.get(0);
            communicator.send(new Request("selectedRecordUser", "post", true, "success"));
        }

        if (type.equals("selectRecord") && actionType.equals("post")) {
            List<Record> records = ServerMain.databaseHandler.findRecords("record_id", (String) data);
            if (records.size() != 1) {
                communicator.send(new Request("selectRecord", "post", true, "failed"));
            }
            selectedRecord = records.get(0);
            communicator.send(new Request("selectRecord", "post", true, "success"));
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
