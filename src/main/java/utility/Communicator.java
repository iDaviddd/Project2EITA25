package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Communicator {
    private BufferedReader reader;
    private PrintWriter writer;

    public Communicator(BufferedReader reader, PrintWriter writer) {
        this.writer = writer;
        this.reader = reader;
    }

    public void send(String message) {
        Gson gson = new Gson();
        System.out.println("Sending: " + message);
        String json = gson.toJson(message);
        writer.write(json + "\n");
        writer.flush();
    }

    public String receive() {
        Gson gson = new Gson();

        String result = "";

        try {
            result = gson.fromJson(reader.readLine(), String.class);
            System.out.println("Received: " + result);
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
}