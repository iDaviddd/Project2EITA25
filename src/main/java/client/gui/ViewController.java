package client.gui;

import client.ClientMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;


public class ViewController extends Stage {
    private Scene scene;
    public Map<String, View> views = new HashMap<>();
    public ObservableMap<String, String> user = FXCollections.observableHashMap();


    public ViewController(ClientMain clientMain) {

        views.put("login", new LoginView(this));
        views.put("patients", new PatientsView(this));
        views.put("records", new RecordView(this));
        views.put("detail", new DetailView(this));
        views.put("create", new CreateRecordView(this));
        scene = new Scene(views.get("login").getParent(), 300, 250);
        this.setTitle("Title");
        this.setScene(scene);
        this.setResizable(true);
        this.show();
    }

    void switchScene(String viewName) {
        views.get(viewName).update();
        this.scene.setRoot(views.get(viewName).getParent());
    }

    void switchScene(String viewName, boolean update) {
        if(update)  views.get(viewName).update();
        this.scene.setRoot(views.get(viewName).getParent());
    }
}
