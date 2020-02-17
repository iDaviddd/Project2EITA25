package client.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import javax.xml.soap.Detail;

public class DetailView {
    private Parent parent;

    DetailView(ViewController viewController){




        Label label = new Label("Name record");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(label.getFont().getName(), 20));
        label.setPadding(new Insets(5,0,10,0));

        BorderPane borderPane = new BorderPane();
        HBox labelBox = new HBox();
        labelBox.setAlignment(Pos.TOP_CENTER);
        labelBox.getChildren().addAll(label);


        borderPane.setTop(labelBox);

        parent = borderPane;

        viewController.setTitle("Record");
    }

    public Parent getParent(){
        return parent;
    }
}
