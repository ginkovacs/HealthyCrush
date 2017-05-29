package healthy.crush.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MenuController implements Initializable
{

	@FXML
	private Button startButton;

	private static Logger logger = LoggerFactory.getLogger(MenuController.class);

	public void initialize(URL location, ResourceBundle resources)
	{
		// startButton.setText("Start from init");
	}

	public void startGame()
	{

		try {
			FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/view/Bar.fxml"));
			AnchorPane root = gameLoader.load();
			Scene scene = new Scene(root);

			Stage gameStage = (Stage) startButton.getScene().getWindow();

			gameStage.setScene(scene);
		}
		catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

	}

}
