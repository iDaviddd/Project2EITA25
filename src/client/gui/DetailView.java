package client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import javax.xml.soap.Detail;

public class DetailView {
    private Parent parent;
    private String patientName = "John Doe";

    DetailView(ViewController viewController){



        Label patientLabel = new Label("Patient: " + patientName);
        patientLabel.setTextAlignment(TextAlignment.CENTER);
        patientLabel.setFont(new Font(patientLabel.getFont().getName(), 20));
        patientLabel.setPadding(new Insets(5,0,10,0));

        TextArea record = new TextArea();
        record.editableProperty().setValue(false);
        record.setText("This is a record\n\n wow very cool");

        Button backButton = new Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                viewController.switchScene("records");
            }
        });

        BorderPane borderPane = new BorderPane();
        VBox labelBox = new VBox();
        labelBox.setAlignment(Pos.TOP_CENTER);
        labelBox.getChildren().addAll(patientLabel, record, backButton);

        borderPane.setTop(labelBox);

        parent = borderPane;

        viewController.setTitle("Record");
    }

    public Parent getParent(){
        return parent;
    }
}
