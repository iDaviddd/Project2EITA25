package client.gui;

import client.network.NetworkHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utility.Request;

public class DetailView implements View {
    private Parent parent;
    private ObservableList<String> recordStrings = FXCollections.observableArrayList("", "");
    private TextField patientText;
    private TextField doctorText;
    private TextField nurseText;
    private TextArea recordText;
    private VBox bodyBox;
    private Button saveButton;
    private Button deleteButton;

    DetailView(ViewController viewController) {

        VBox headerBox = new VBox();
        HBox patientBox = new HBox();
        HBox doctorBox = new HBox();
        HBox nurseBox = new HBox();

        patientText = new TextField("test");
        doctorText = new TextField("test");
        nurseText = new TextField("test");

        patientText.editableProperty().setValue(false);
        doctorText.editableProperty().setValue(false);
        nurseText.editableProperty().setValue(false);

        Label patientLabel = new Label("Patient: ");
        Label doctorLabel = new Label("Doctor: ");
        Label nurseLabel = new Label("Nurse: ");

        headerBox.getChildren().addAll(patientBox, doctorBox, nurseBox);

        patientBox.getChildren().addAll(patientLabel, patientText);
        doctorBox.getChildren().addAll(doctorLabel, doctorText);
        nurseBox.getChildren().addAll(nurseLabel, nurseText);

        recordText = new TextArea();
        recordText.setText("");
        recordText.editableProperty().setValue(false);
        Button backButton = new Button("Back");

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                viewController.switchScene("records");
            }
        });

        saveButton = new Button("Save");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NetworkHandler.communicator.send(new Request("record", "post", recordText.getText()));
                if (NetworkHandler.communicator.receive().type.equals("error")) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Not allowed to write to record");
                    a.show();
                } else {
                    viewController.switchScene("records");
                }
            }
        });

        deleteButton = new Button("Delete");
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NetworkHandler.communicator.send(new Request("record", "delete", ""));
                if (NetworkHandler.communicator.receive().type.equals("error")) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Not allowed to delete record");
                    a.show();
                } else {
                    viewController.switchScene("records");
                }
            }
        });

        BorderPane borderPane = new BorderPane();
        bodyBox = new VBox();
        bodyBox.getChildren().addAll(recordText, backButton);

        borderPane.setTop(headerBox);
        borderPane.setCenter(bodyBox);
        parent = borderPane;

        viewController.setTitle("Record");
    }

    public Parent getParent() {
        return parent;
    }

    @Override
    public void update() {
        if ((ViewController.role.equals("Doctor") || ViewController.role.equals("Nurse")) && !bodyBox.getChildren().contains(saveButton)) {
            recordText.editableProperty().setValue(true);
            bodyBox.getChildren().add(saveButton);
        }
        if (ViewController.role.equals("Government") && !bodyBox.getChildren().contains(deleteButton)) {
            bodyBox.getChildren().add(deleteButton);
        }

        NetworkHandler.communicator.send(new Request("record", "get", ""));
        Request r = NetworkHandler.communicator.receive();

        if (r.type.equals("error")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Not allowed to view record");
            a.show();
        } else {
            String patientName = r.data;

            String patientNumber = NetworkHandler.communicator.receive().data;

            String doctorName = NetworkHandler.communicator.receive().data;
            String doctorNumber = NetworkHandler.communicator.receive().data;

            String nurseName = NetworkHandler.communicator.receive().data;
            String nurseNumber = NetworkHandler.communicator.receive().data;

            String record = NetworkHandler.communicator.receive().data;

            patientText.setText(patientName + " " + patientNumber);
            doctorText.setText(doctorName + " " + doctorNumber);
            nurseText.setText(nurseName + " " + nurseNumber);

            recordText.setText(record);
        }
    }
}
