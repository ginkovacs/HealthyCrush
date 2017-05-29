package healthy.crush.Main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The purpose of this class is to launch the main menu window.
 * 
 * @author Georgina Kovács
 *
 */
public class Main extends Application
{

	private static Logger logger = LoggerFactory.getLogger(Main.class);

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
			logger.error(e.getMessage(), e);
		}
	}

	public static void main(String[] args)
	{
		launch(args);

	}

}
