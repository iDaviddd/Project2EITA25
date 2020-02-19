package client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

public class RecordView {

    private Parent parent;
    private String recordChosen;

    RecordView(ViewController viewController){
        Label label = new Label("Records");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(label.getFont().getName(), 20));
        label.setPadding(new Insets(5,0,10,0));

        ListView<String> listView = new ListView<>();
        listView.getItems().add("Test 1");
        listView.getItems().add("Test 2");
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

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(listView.getSelectionModel().getSelectedItem());
                chooseButton.setDisable(false);
            }
        });

        chooseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                recordChosen = listView.getSelectionModel().getSelectedItem();
                viewController.switchScene("detail");
            }
        });
    }

    public Parent getParent(){
        return parent;
    }
}
