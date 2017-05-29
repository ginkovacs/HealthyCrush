package healthy.crush.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import healthy.crush.model.Cell;
import healthy.crush.model.CellImageView;
import healthy.crush.model.Game;
import healthy.crush.model.GameImages;
import healthy.crush.model.PositionImageView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameController implements Initializable
{

	@FXML
	private Button surButton;

	@FXML
	private GridPane gameGridPane;

	@FXML
	private Label gameScoreLabel;

	@FXML
	private Label gameHpLabel;

	@FXML
	private Label gameMinPointLabel;

	private static Logger logger = LoggerFactory.getLogger(GameController.class);

	List<CellImageView>	cellImageViewList	= new ArrayList<>();
	CellImageView		first				= new CellImageView();

	boolean	isFirst	= true;
	Game	game	= new Game(7, 0, 10, 10);

	DropShadow dropShadow = new DropShadow(60, Color.IVORY);

	public void initialize(URL location, ResourceBundle resources)
	{

		game.genMap();
		game.update();

		Cell[][] map = game.getGameMap();

		gameHpLabel.setText(String.valueOf(game.getHp()));
		gameMinPointLabel.setText(String.valueOf(game.getMinPoint()));

//		for (int i = 0; i < 7; i++) {
//			for (int j = 0; j < 7; j++) {
//				System.out.print(map[i][j] + "\t");
//			}
//			System.out.println();
//		}
//		System.out.println();

		for (int row = 0; row < 7; row++) {
			for (int col = 0; col < 7; col++) {

				PositionImageView positionImageView = new PositionImageView(row, col);
				CellImageView cellImageView = (CellImageView) nodeFromGridPane(gameGridPane, col, row);
				cellImageView.setPosition(positionImageView);
				cellImageViewList.add(cellImageView);

				cellImageView.setImage(GameImages.getInstance().getImage(map[row][col]));

				cellImageView.setOnMouseClicked(event -> {
					if(event.getButton() == MouseButton.PRIMARY) {
						if(isFirst) {
							first = (CellImageView) event.getTarget();
							isFirst = false;
							cellImageView.setEffect(dropShadow);
						}
						else {
							if(first != event.getTarget()) {
								CellImageView second = (CellImageView) event.getTarget();

								int firstRow = first.getPosition().getRow();
								int firstCol = first.getPosition().getCol();
								int secondRow = second.getPosition().getRow();
								int secondCol = second.getPosition().getCol();

								isFirst = true;
								if((firstRow == secondRow && (firstCol == secondCol - 1 || firstCol == secondCol + 1))
										|| (firstCol == secondCol
												&& (firstRow == secondRow - 1 || firstRow == secondRow + 1))) {
									game.canSwapFromTo(firstRow, firstCol,
											secondRow, secondCol);
									game.swap(firstRow, firstCol, secondRow,
											secondCol);
								}

								update();
							}
							cellImageView.setEffect(null);
						}

					}
					game.setHp(game.getHp() - 1);

				});

			}
		}

		update();
		map = game.getGameMap();

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				System.out.print(map[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println(".");
	}

	public void surrenderGame()
	{
		try {

			FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/view/mainMenu.fxml"));
			AnchorPane root = menuLoader.load();
			Scene scene = new Scene(root);

			Stage menuStage = (Stage) surButton.getScene().getWindow();

			menuStage.setScene(scene);
		}
		catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private Node nodeFromGridPane(GridPane gameGridPane, int col, int row)
	{
		for (Node node : gameGridPane.getChildren()) {
			if(GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
				return node;
			}
		}
		return null;
	}

//	private void swap(CellImageView first, CellImageView second)
//	{
//		PositionImageView firstPosition = first.getPosition();
//		PositionImageView secondPosition = second.getPosition();
//
//		gameGridPane.getChildren().remove(first);
//		gameGridPane.getChildren().remove(second);
//		gameGridPane.add(first, secondPosition.getCol(), secondPosition.getRow());
//		gameGridPane.add(second, firstPosition.getCol(), firstPosition.getRow());
//
//		first.setPosition(secondPosition);
//		second.setPosition(firstPosition);
//
//	}
	private void update()
	{

		while (game.delete()) {
			updateui();
//			PauseTransition pause = new PauseTransition(Duration.seconds(1));
//			pause.setOnFinished(event -> game.del());
//			pause.play();
//			game.del();
			game.reworkGameMap();

			if(game.getHp() == 0) {
				if(game.getScore() >= game.getMinPoint())
					logger.info("You won! :");
				else
					logger.info("You lost... :(");
			}

			updateui();
		}
	}

	private void updateui()
	{
		Cell[][] map = game.getGameMap();

		for (int row = 0; row < 7; row++) {
			for (int col = 0; col < 7; col++) {
				CellImageView cellImageView = (CellImageView) nodeFromGridPane(gameGridPane, col, row);
				cellImageView.setImage(GameImages.getInstance().getImage(map[row][col]));
			}
		}
		gameScoreLabel.setText(String.valueOf(game.getScore()));
		gameHpLabel.setText(String.valueOf(game.getHp()));

	}

}
