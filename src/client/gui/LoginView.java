package client.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public class LoginView {

    private Parent parent;

    LoginView(ViewController viewController){




        Label label = new Label("Login");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(label.getFont().getName(), 20));
        label.setPadding(new Insets(5,0,10,0));

        BorderPane borderPane = new BorderPane();
        HBox labelBox = new HBox();
        labelBox.setAlignment(Pos.TOP_CENTER);
        labelBox.getChildren().addAll(label);


        borderPane.setTop(labelBox);

        parent = borderPane;

        viewController.setTitle("Title");
    }

    /**
     * Returns the parent of the current scene. Required by Register.Gui.ParentGenerator. Used to retrieve panel for switching scenes.
     * @return scene panel.
     */
    public Parent getParent(){
        return parent;
    }


}
