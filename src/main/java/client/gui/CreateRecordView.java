package client.gui;

import client.network.NetworkHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import utility.Request;


public class CreateRecordView implements View{
    private Parent parent;

    CreateRecordView(ViewController viewController){

        Label label = new Label("Create record");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(label.getFont().getName(), 20));
        label.setPadding(new Insets(5,0,10,0));

        BorderPane borderPane = new BorderPane();

        ButtonBar buttons = new ButtonBar();
        Button backButton = new Button("Back");
        Button createButton = new Button("Create");

        buttons.getButtons().addAll(backButton, createButton);

        VBox container = new VBox(5);
        container.setPadding(new Insets(5,5,5,5));
        Label textNurse = new Label("Nurse's personal identity number");
        TextField nurse = new TextField();
        Label textRecord = new Label("Record");
        TextArea record = new TextArea();

        container.getChildren().addAll(textNurse, nurse, textRecord, record, buttons);


        borderPane.setCenter(container);

        parent = borderPane;

        viewController.setTitle("Create");

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                viewController.switchScene("records", false);
            }
        });

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NetworkHandler.communicator.send(new Request("selectNurse", "post", nurse.getText()));
                NetworkHandler.communicator.receive();
                NetworkHandler.communicator.send(new Request("add_record", "post", record.getText()));

                if(NetworkHandler.communicator.receive().type.equals("error")){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Not allowed to add record to this user");
                    a.show();
                }
                else{
                    viewController.switchScene("records");
                }
            }
        });
    }

    @Override
    public void update() {

    }

    public Parent getParent(){
        return parent;
    }
}
