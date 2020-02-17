package client.gui;

import client.ClientMain;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;


public class ViewController extends Stage {
    private Scene scene;
    private Map<String, Parent> views = new HashMap<>();


    public ViewController(ClientMain clientMain) {

        views.put("login", new LoginView(this).getParent());
        views.put("records", new RecordView(this).getParent());
        views.put("detail", new DetailView(this).getParent());
        views.put("create", new CreateRecordView(this).getParent());
        scene = new Scene(views.get("login"), 300, 250);
        this.setTitle("Title");
        this.setScene(scene);
        this.setResizable(true);
        this.show();
    }

    void switchScene(String viewName) {
        scene.setRoot(views.get(viewName).getParent());
    }
}
