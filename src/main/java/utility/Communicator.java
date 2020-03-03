package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSyntaxException;

public class Communicator {
    private BufferedReader reader;
    private PrintWriter writer;

    public Communicator(BufferedReader reader, PrintWriter writer) {
        this.writer = writer;
        this.reader = reader;
    }

    public void send(Request request) {
        Gson gson = new Gson();
        System.out.println("Sending: " + request);
        String json = gson.toJson(request);
        writer.write(json + "\n");
        writer.flush();
    }

    public Request receive(){
        Gson gson = new Gson();

        Request result = null;

        try {
            result = gson.fromJson(reader.readLine(), Request.class);
            if(result != null){
                System.out.println("Received: " + result.type + " " + result.actionType + " " + result.data);
            }
            else {
                System.out.println("Received: " + result);
            }

        }
        catch (JsonSyntaxException e) {
            System.out.println("Json syntax error");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("Can't read");
            e.printStackTrace();
        }
        return result;
    }

    public List receive(JsonDeserializer deserializer) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(List.class, deserializer).create();

        List list = gson.fromJson(reader.readLine(), List.class);
        return list;
    }


}