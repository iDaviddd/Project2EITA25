package utility;

public class Request {

    public String type;
    public String actionType;
    public String data;

    public Request(String type, String actionType, String data){
        this.type = type;
        this.actionType = actionType;
        this.data = data;
    }
}
