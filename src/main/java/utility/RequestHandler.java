package utility;

import client.network.NetworkHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.ServerMain;
import server.database.DatabaseHandler;
import server.network.LoginHandler;

import java.util.List;


public class RequestHandler {

    static  GsonBuilder builder = new GsonBuilder();
    static Gson gson = builder.create();

    public static void  processRequest(String string, Communicator communicator){
        Request request = gson.fromJson(string, RequestHandler.Request.class);

        switch (request.requestType){
            case "get_records":
                break;
            default:
                break;
        }
    }

   public static class Request {
        public String requestType;
        public String data;

        Request(String requestType, String data){
            this.requestType = requestType;
            this.data = data;
        }
    }


}
