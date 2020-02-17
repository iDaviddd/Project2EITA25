package client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public class LoginView {

    private Parent parent;
    private String user;
    private String pass;

    LoginView(ViewController viewController){




        Label label = new Label("Login");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(label.getFont().getName(), 20));
        label.setPadding(new Insets(5,0,10,0));


        BorderPane borderPane = new BorderPane();
        VBox loginText = new VBox(5);
        Label textUsername = new Label("Username");
        TextField username = new TextField();
        username.setPromptText("username");
        loginText.setPadding(new Insets(10, 50, 0, 50));
        Label textPassword = new Label("Password");
        PasswordField password = new PasswordField();
        password.setPromptText("password");

        Button loginButton = new Button("Login");

        loginText.getChildren().addAll(textUsername, username, textPassword, password, loginButton);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               user = username.getText();
               pass = password.getText();
            }
        });





        borderPane.setCenter(loginText);

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
