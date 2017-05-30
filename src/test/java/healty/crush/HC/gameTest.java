package healty.crush.HC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import java.util.Arrays;

import org.junit.Test;

import healthy.crush.model.Cell;
import healthy.crush.model.Game;

public class gameTest
{
	Game game;

	@Test
	public void checkIfMapIsEmpty()
	{
		game = spy(new Game(7, 10, 50));
		doNothing().when(game).genStep();
		doNothing().when(game).update();

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				assertNotNull(game.getGameMap()[i][j]);
			}
		}

	}

	@Test
	public void swapsTwoPositions()
	{
		game = spy(new Game(7, 10, 50));
		doNothing().when(game).genStep();
		doNothing().when(game).update();

		game.genMap();

		Cell first = game.getGameMap()[0][0];
		Cell second = game.getGameMap()[0][1];

		game.swap(0, 0, 0, 1);

		assertEquals(first, game.getGameMap()[0][1]);
		assertEquals(second, game.getGameMap()[0][0]);
	}

	@Test
	public void checkDeleteVeggies()
	{
		game = spy(new Game(7, 10, 50));

		Cell[][] onion = new Cell[7][7];

		for (Cell[] row : onion)
			Arrays.fill(row, Cell.EMPTY);

		game.genMap();
		Cell[][] gameMap = game.getGameMap();

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if(gameMap[i][j].equals(Cell.ONION)) {
					onion[i][j] = Cell.ONION;
				}
			}
		}

		game.deleteAllVeggiesOfSameType(Cell.ONION);

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if(onion[i][j].equals(Cell.ONION)) {
					assertTrue(game.getGameMapMask()[i][j]);
				}
			}
		}
	}

	@Test
	public void checkStep()
	{
		game = spy(new Game(7, 10, 50));

		game.genMap();

		game.getGameMap()[3][3] = Cell.ONION;
		game.getGameMap()[3][5] = Cell.ONION;
		game.getGameMap()[3][6] = Cell.ONION;

		Cell first = game.getGameMap()[3][3];
		Cell second = game.getGameMap()[3][4];

		game.step(3, 3, 3, 4);

		assertEquals(first, game.getGameMap()[3][4]);
		assertEquals(second, game.getGameMap()[3][3]);

		game.getGameMap()[0][0] = Cell.ONION;
		game.getGameMap()[0][2] = Cell.ONION;
		game.getGameMap()[0][3] = Cell.ONION;

		first = game.getGameMap()[0][0];
		second = game.getGameMap()[0][1];

		game.step(3, 3, 3, 4);

		assertEquals(first, game.getGameMap()[0][0]);
		assertEquals(second, game.getGameMap()[0][1]);

	}

}
