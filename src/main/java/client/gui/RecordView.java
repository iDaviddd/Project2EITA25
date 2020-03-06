package client.gui;

import client.network.NetworkHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import utility.Request;

import java.util.ArrayList;
import java.util.List;

public class RecordView implements View {

    private Parent parent;
    private String recordChosen;
    private ObservableList<String> recordsString = FXCollections.observableArrayList();
    private HBox buttonBox;
    private Button newRecordButton;

    RecordView(ViewController viewController) {
        Label label = new Label("Records");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(label.getFont().getName(), 20));
        label.setPadding(new Insets(5, 0, 10, 0));

        ListView<String> listView = new ListView<>();
        listView.setItems(recordsString);
        listView.setPrefHeight(150);

        BorderPane borderPane = new BorderPane();
        VBox labelBox = new VBox();
        labelBox.setAlignment(Pos.TOP_CENTER);
        labelBox.getChildren().addAll(label);
        labelBox.getChildren().addAll(listView);

        Button chooseButton = new Button("Choose");
        chooseButton.setDisable(true);

        Button backButton = new Button("Back");
        newRecordButton = new Button("New record");

        borderPane.setTop(labelBox);
        buttonBox = new HBox();
        buttonBox.getChildren().add(backButton);
        buttonBox.getChildren().add(chooseButton);

        borderPane.setBottom(buttonBox);

        parent = borderPane;

        viewController.setTitle("Records");

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                chooseButton.setDisable(false);
            }
        });

        chooseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NetworkHandler.communicator.send(new Request("selectRecord", "post", listView.getSelectionModel().getSelectedItem()));
                if (NetworkHandler.communicator.receive().data.equals("success")) {
                    viewController.switchScene("detail");
                } else {
                    //failed
                }
            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                viewController.switchScene("patients", false);
            }
        });

        newRecordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                viewController.switchScene("create", false);
            }
        });
    }

    public Parent getParent() {
        return parent;
    }

    @Override
    public void update() {
        if (ViewController.role.equals("Doctor") && !buttonBox.getChildren().contains(newRecordButton)) {
            buttonBox.getChildren().add(newRecordButton);
        }

        NetworkHandler.communicator.send(new Request("records", "get", ""));

        Request response = NetworkHandler.communicator.receive();
        List<String> records = new ArrayList<>();
        if (response.type.equals("records") && response.actionType.equals("get") && response.reply && response.data.equals("start")) {
            response = NetworkHandler.communicator.receive();
            while (response.data != null) {
                records.add((String) response.data);
                response = NetworkHandler.communicator.receive();
            }
        } else {
        }

        recordsString.setAll(records);
    }
}
