package client.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public class CreateRecordView implements View{
    private Parent parent;

    CreateRecordView(ViewController viewController){

        Label label = new Label("Create record");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(label.getFont().getName(), 20));
        label.setPadding(new Insets(5,0,10,0));

        BorderPane borderPane = new BorderPane();

        ButtonBar buttons = new ButtonBar();
        Button back = new Button("Back");
        Button create = new Button("Create");

        buttons.getButtons().addAll(back, create);

        VBox container = new VBox(5);
        container.setPadding(new Insets(5,5,5,5));
        Label textPatient = new Label("Patient name");
        TextField patient = new TextField();
        Label textPersonalNumber = new Label("Patient's personal identity number");
        TextField personalNumber = new TextField();
        Label textNurse = new Label("Nurse");
        TextField nurse = new TextField();
        Label textRecord = new Label("Record");
        TextArea record = new TextArea();

        container.getChildren().addAll(textPatient, patient, textPersonalNumber, personalNumber, textNurse, nurse, textRecord, record, buttons);


        borderPane.setCenter(container);

        parent = borderPane;

        viewController.setTitle("Create");
    }

    @Override
    public void update() {

    }

    public Parent getParent(){
        return parent;
    }
}
