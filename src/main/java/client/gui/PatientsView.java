package client.gui;

import client.network.NetworkHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import utility.Request;
import utility.User;
import java.util.*;

public class PatientsView implements View{
    private List<User> patients;
    private final ObservableList<String> patientsString = FXCollections.observableArrayList();
    private final Parent parent;



    PatientsView(ViewController viewController){
        Label label = new Label("Patients");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(label.getFont().getName(), 20));
        label.setPadding(new Insets(5,0,10,0));

        ListView<String> listView = new ListView<>();
        listView.setItems(patientsString);
        listView.setPrefHeight(150);

        BorderPane borderPane = new BorderPane();
        VBox labelBox = new VBox();
        labelBox.setAlignment(Pos.TOP_CENTER);
        labelBox.getChildren().addAll(label);
        labelBox.getChildren().addAll(listView);

        Button chooseButton = new Button("Choose");
        chooseButton.setDisable(true);

        borderPane.setTop(labelBox);
        borderPane.setBottom(chooseButton);

        parent = borderPane;

        viewController.setTitle("Records");

        listView.setOnMouseClicked(event -> chooseButton.setDisable(false));

        chooseButton.setOnAction(event -> {
            NetworkHandler.communicator.send(new Request("selectRecordUser", "post", listView.getSelectionModel().getSelectedItem()));
            if(NetworkHandler.communicator.receive().data.equals("success")){
                viewController.switchScene("records");
            }
            //failed


        });
    }

    public Parent getParent(){
        return parent;
    }

    @Override
    public void update()  {
        NetworkHandler.communicator.send(new Request("users", "get", "patients"));

        Request response = NetworkHandler.communicator.receive();
        List<String> personalNumbers = new ArrayList<>();
        if(response.type.equals("users") && response.actionType.equals("get") && response.reply  && response.data.equals("start")){
            response = NetworkHandler.communicator.receive();
            while (response.data != null){
                personalNumbers.add(response.data);
                response = NetworkHandler.communicator.receive();
            }
        }

        patientsString.setAll(personalNumbers);
    }
}
