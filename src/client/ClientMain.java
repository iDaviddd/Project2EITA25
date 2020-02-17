package client;

import client.gui.ViewController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		ViewController viewController = new ViewController(this);
	}
}
