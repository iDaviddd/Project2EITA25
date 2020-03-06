package client;

import client.gui.ViewController;
import client.network.NetworkHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {

	@Override
	public void start(Stage primaryStage) throws InterruptedException {
		new ViewController();

		String host = "localhost";
		int port = 9876;

		new NetworkHandler(host, port);

	}

}
