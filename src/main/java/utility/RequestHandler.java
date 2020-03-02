package utility;

import client.network.NetworkHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.ServerMain;
import server.database.DatabaseHandler;

import java.util.HashSet;
import java.util.List;


public class RequestHandler {

    static  GsonBuilder builder = new GsonBuilder();
    static Gson gson = builder.create();

    public static void processRequest(Request request, User user, Communicator communicator){

        String type = request.type;
        String actionType = request.actionType;
        Object data = request.data;
        boolean reply = request.reply;
        User selectedRecordUser = null;

        if(type.equals("records") && actionType.equals("get")){

            List<Record> records = getRecordsOfUser(selectedRecordUser);

            System.out.println(records);
            communicator.send(new Request("records", "get", true, "start"));
            records.forEach(a -> communicator.send(new Request("records", "get", true, a.getDoctorPersonalNumber())));
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
        if(type.equals("selectRecordUser") && actionType.equals("post")){
            List<User> users = ServerMain.databaseHandler.findUsers("personal_number", (String) data);
            System.out.println(users);
            if(users.size() != 1){
                communicator.send(new Request("selectedRecordUser", "post", true, "failed"));
            }
            selectedRecordUser = users.get(0);
            communicator.send(new Request("selectedRecordUser", "post", true, "success"));
        }
    }

    private static List<User> getPatients(){
        System.out.println("getPatients");
        return ServerMain.databaseHandler.findUsers("role","Patient");
    }

    private static List<Record> getRecordsOfUser(User user){
        System.out.println("Records:");
        List<Record> records = null;
        if(user.getRole().equals("Patient")){
            records = ServerMain.databaseHandler.findRecords("patient_personal_number", user.getPersonalNumber());
            System.out.println(records);
        }
        return records;
    }


}
