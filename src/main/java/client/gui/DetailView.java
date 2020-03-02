package client.gui;

import client.network.NetworkHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

        Button backButton = new Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                viewController.switchScene("records");
            }
        });

        BorderPane borderPane = new BorderPane();
        VBox bodyBox = new VBox();
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
        NetworkHandler.communicator.send(new Request("record", "get", ""));

        String patientName = NetworkHandler.communicator.receive().data;
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
