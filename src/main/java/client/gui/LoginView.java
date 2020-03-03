package client.gui;

import client.network.NetworkHandler;
import utility.Hasher;
import utility.Request;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public class LoginView implements View{

    private Parent parent;

    LoginView(ViewController viewController){

        Label label = new Label("Login");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(label.getFont().getName(), 20));
        label.setPadding(new Insets(5,0,10,0));


        BorderPane borderPane = new BorderPane();

        VBox loginText = new VBox(5);
        Label textUsername = new Label("Username");
        final TextField username = new TextField();
        username.setPromptText("username");
        loginText.setPadding(new Insets(10, 50, 0, 50));

        Label textPassword = new Label("Password");
        final PasswordField password = new PasswordField();
        password.setPromptText("password");

        Label textOTP= new Label("OTP");
        final TextField OTP = new TextField();
        OTP.setPromptText("OTP");
        textOTP.setPadding(new Insets(10, 50, 0, 50));

        Button loginButton = new Button("Login");

        loginText.getChildren().addAll(textUsername, username, textPassword, password, textOTP, OTP,  loginButton);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(login(username.getText(), password.getText(), OTP.getText())){
                    System.out.println("Authenticated");
                    viewController.switchScene("patients");
                }
                else{
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Wrong username, password or OTP");
                    a.show();
                }
            }
        });

        borderPane.setCenter(loginText);

        parent = borderPane;

        viewController.setTitle("Login");
    }

    /**
     * Returns the parent of the current scene. Required by Register.Gui.ParentGenerator. Used to retrieve panel for switching scenes.
     * @return scene panel.
     */
    public Parent getParent(){
        return parent;
    }

    private boolean login(String username, String password, String OTP){
        NetworkHandler.communicator.send(new Request("username", "post", username));

        Request salt = NetworkHandler.communicator.receive();
        if(!salt.type.equals("salt")){
            System.out.println("Login did not receive salt");
            return false;
        }
        Request challenge = NetworkHandler.communicator.receive();
        if(!challenge.type.equals("challenge")){
            System.out.println("Login did not receive challenge");
            return false;
        }

        String hashedPassword = Hasher.HashPassword(password, (String) salt.data);
        String response = Hasher.HashPassword(hashedPassword, (String) challenge.data);

        NetworkHandler.communicator.send(new Request("challenge response", "post", response));
        NetworkHandler.communicator.send(new Request("OTP", "post", OTP));

        Request authentication = NetworkHandler.communicator.receive();
        if(authentication.type.equals("authentication") && authentication.data.equals("true")){
            ViewController.role = NetworkHandler.communicator.receive().data;
            return true;
        }
        return false;
    }

    @Override
    public void update() {

    }
}
