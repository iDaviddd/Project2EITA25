package utility;

import java.util.List;

public class Request {

    public String type;
    public String actionType;
    public boolean reply;
    public String data;


    public Request(String type, String actionType, String data){
        this.type = type;
        this.actionType = actionType;
        this.data = data;
        reply = false;
    }
    public Request(String type, String actionType, boolean reply, String data){
        this.type = type;
        this.actionType = actionType;
        this.reply = reply;
        this.data = data;
    }
}
