package healthy.crush.Main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application
{

	@Override
	public void start(Stage primaryStage)
	{
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/view/mainMenu.fxml"));
			AnchorPane rootContainer = (AnchorPane) loader.load();
			Scene scene = new Scene(rootContainer);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);

	}

}
