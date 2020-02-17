package client.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class CreateRecordView {
    private Parent parent;

    CreateRecordView(ViewController viewController){




        Label label = new Label("Create record");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(label.getFont().getName(), 20));
        label.setPadding(new Insets(5,0,10,0));

        BorderPane borderPane = new BorderPane();
        HBox labelBox = new HBox();
        labelBox.setAlignment(Pos.TOP_CENTER);
        labelBox.getChildren().addAll(label);


        borderPane.setTop(labelBox);

        parent = borderPane;

        viewController.setTitle("Create");
    }

    public Parent getParent(){
        return parent;
    }
}
