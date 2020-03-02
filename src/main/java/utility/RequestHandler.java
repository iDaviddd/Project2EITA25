package utility;

import client.network.NetworkHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.ServerMain;
import server.database.DatabaseHandler;
import server.network.LoginHandler;

import java.util.HashSet;
import java.util.List;


public class RequestHandler {

    static  GsonBuilder builder = new GsonBuilder();
    static Gson gson = builder.create();

    public static void  processRequest(Request request, User user){

        if(request.type.equals("records") && request.actionType.equals("get")){
            getRecordsOfUser(user);
        }
    }

    private static HashSet<Record> getRecordsOfUser(User user){
        System.out.println("Records:");
        HashSet<Record> records = null;
        if(user.getRole().equals("Patient")){
            records = ServerMain.databaseHandler.findRecords("patient_personal_number", user.getPersonalNumber());
            System.out.println(records);
        }
        return records;
    }


}
