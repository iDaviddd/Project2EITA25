package utility;

public class Request {

    public final String type;
    public final String actionType;
    public final boolean reply;
    public final String data;


    public Request(String type, String actionType, String data) {
        this.type = type;
        this.actionType = actionType;
        this.data = data;
        reply = false;
    }

    public Request(String type, String actionType, boolean reply, String data) {
        this.type = type;
        this.actionType = actionType;
        this.reply = reply;
        this.data = data;
    }
}
