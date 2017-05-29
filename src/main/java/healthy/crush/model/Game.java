package healthy.crush.model;

import java.util.Arrays;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import healthy.crush.Main.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Game
{
	Cell[][]			gameMap;
	int					score;
	int					hp;
	int					mapSize;
	int					minPoint;
	private Timeline	timer;
	boolean[][]			gameMapMask;

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public Game(int mapSize, int score, int hp, int minPoint)
	{
		gameMap = new Cell[mapSize][mapSize];

		gameMapMask = new boolean[mapSize][mapSize];
		for (Cell[] row : gameMap)
			Arrays.fill(row, Cell.EMPTY);

		this.score = score;

		this.hp = hp;

		this.mapSize = mapSize;

		this.minPoint = minPoint;
	}

	/**
	 * A függvény megad legalább egy lehetséges lépést, amit a játék kezdetekor végre lehet hajtani.
	 */
	public void genStep()
	{
		Random rand = new Random();
		Cell firstVeggie = Cell.values()[rand.nextInt(5) + 1];

		int row = rand.nextInt(mapSize - 2) + 1;
		int col = rand.nextInt(mapSize - 2) + 1;

		gameMap[row][col] = firstVeggie;

		int r = rand.nextInt(2);

		if(r == 0) {
			gameMap[row][col - 1] = firstVeggie;
			gameMap[row - 1][col + 1] = firstVeggie;
		}
		else {
			gameMap[row][col - 1] = firstVeggie;
			gameMap[row + 1][col + 1] = firstVeggie;
		}
	}

	/**
	 * Generál egy véletlenszerű Cell típusú táblát.
	 */
	public void genMap()
	{
		Random rand = new Random();
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				gameMap[i][j] = Cell.values()[rand.nextInt(5) + 1];
			}
		}
		genStep();
		update();
	}

	public void gameTimer()
	{
		timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {

		}));
		timer.setCycleCount(1);
		timer.play();
	}

	/**
	 * Megadja, hogy az adott kordináták körül, kereszt alakban legalább 3 ugyanolyan típus van-e egymás mellett.
	 * 
	 * @param x x koordináta
	 * @param y y koordináta
	 * @return igaz-e, hogy legalább 3 ugyanolyan típus van-e egymás mellett
	 */

	public boolean crossTest(int x, int y)
	{
		Cell cellForTest = gameMap[x][y];
		int eqInRow = 0;

		// FÜGGŐLEGES
		// első sor
		if(x == 0) {
			for (int i = 1; i < 3; i++) {
				if(gameMap[x + i][y].equals(cellForTest)) {
					eqInRow++;
				}
				else {
					eqInRow = 0;
				}
			}
			if(eqInRow >= 3)
				return true;
		}

		// második sor
		else if(x == 1) {
			for (int i = -1; i < 3; i++) {
				if(gameMap[x + i][y].equals(cellForTest)) {
					eqInRow++;
				}
				else {
					eqInRow = 0;
				}
			}
			if(eqInRow >= 3)
				return true;
		}

		// utolsó előtti sor
		else if(x == mapSize - 2) {
			for (int i = -2; i < 2; i++) {
				if(gameMap[x + i][y].equals(cellForTest)) {
					eqInRow++;
				}
				else {
					eqInRow = 0;
				}
			}
			if(eqInRow >= 3)
				return true;
		}

		// utolsó sor
		else if(x == mapSize - 1) {
			for (int i = -2; i < 1; i++) {
				if(gameMap[x + i][y].equals(cellForTest)) {
					eqInRow++;
				}
				else {
					eqInRow = 0;
				}
			}
			if(eqInRow >= 3)
				return true;
		}

		// többi sor
		else {
			for (int i = -2; i < 3; i++) {
				if(gameMap[x + i][y].equals(cellForTest)) {
					eqInRow++;
				}
				else {
					eqInRow = 0;
				}
			}
			if(eqInRow >= 3)
				return true;
		}

		// VIZSZINTES
		eqInRow = 0;

		// első oszlop
		if(y == 0) {
			for (int i = 1; i < 3; i++) {
				if(gameMap[x][y + i].equals(cellForTest)) {
					eqInRow++;
				}
				else {
					eqInRow = 0;
				}
			}
			if(eqInRow >= 3)
				return true;
		}

		// második oszlop
		else if(y == 1) {
			for (int i = -1; i < 3; i++) {
				if(gameMap[x][y + i].equals(cellForTest)) {
					eqInRow++;
				}
				else {
					eqInRow = 0;
				}
			}
			if(eqInRow >= 3)
				return true;
		}

		// utolsó előtti oszlop
		else if(y == mapSize - 2) {
			for (int i = -2; i < 2; i++) {
				if(gameMap[x][y + i].equals(cellForTest)) {
					eqInRow++;
				}
				else {
					eqInRow = 0;
				}
			}
			if(eqInRow >= 3)
				return true;
		}

		// utolsó oszlop
		else if(y == mapSize - 1) {
			for (int i = -2; i < 1; i++) {
				if(gameMap[x][y + i].equals(cellForTest)) {
					eqInRow++;
				}
				else {
					eqInRow = 0;
				}
			}
			if(eqInRow >= 3)
				return true;
		}

		else {
			for (int i = -2; i < 3; i++) {
				if(gameMap[x][y + i].equals(cellForTest)) {
					eqInRow++;
				}
				else {
					eqInRow = 0;
				}
			}
			if(eqInRow >= 3)
				return true;
		}
		return false;
	}

	/**
	 * Megcseréli a koordinátákat.
	 * 
	 * @param fromX kezdő x koordináta
	 * @param fromY kezdő y koordináta
	 * @param toX vég x koordináta
	 * @param toY vég y koordináta
	 */
	public void swap(int fromX, int fromY, int toX, int toY)
	{
		Cell temp = gameMap[fromX][fromY];
		gameMap[fromX][fromY] = gameMap[toX][toY];
		gameMap[toX][toY] = temp;
//		for (int i = 0; i < mapSize; i++) {
//			for (int j = 0; j < mapSize; j++) {
//				System.out.print(gameMap[i][j] + "\t");
//			}
//			System.out.println();
//		}
	}

	/**
	 * Megadja, hogy egymás mellett vannak-e az elemek és ki lehet-e cserélni a két koordinátán lévő elemeket.
	 * 
	 * @param fromX kezdő x koordináta
	 * @param fromY kezdő y koordináta
	 * @param toX vég x koordináta
	 * @param toY vég y koordináta
	 * @return igaz-e, hogy meg lehet cserélni a két koordinátán lévő elemeket
	 */
	public boolean canSwapFromTo(int fromX, int fromY, int toX, int toY)
	{
		boolean canSwap = false;

		swap(fromX, fromY, toX, toY);

		if(crossTest(toX, toY) || crossTest(fromX, fromY)) {
			canSwap = true;
			// swap(fromX, fromY, toX, toY);
		}
		else
			canSwap = false;
		swap(toX, toY, fromX, fromY);
		return canSwap;

	}

	/**
	 * A játéktábla maszk-jában beállítja, hogy mely elemek tartoznak ugyanolyan típusba.
	 * 
	 * @param gameMapMask a játéktábla maszk-ja
	 * @param type megadott Cell típus
	 */
	public void deleteAllVeggiesOfSameType(boolean[][] gameMapMask, Cell type)
	{
		for (int row = 0; row < mapSize; row++)
			for (int col = 0; col < mapSize; col++) {
				if(gameMap[row][col].equals(type))
					gameMapMask[row][col] = true;
			}

	}

	/**
	 * 
	 * 
	 * @param gameMapMask a játéktábla maszk-ja
	 */
	public void rowMask(boolean[][] gameMapMask)
	{

		for (int row = 0; row < mapSize; row++) {
			int count = 0;
			for (int col = 0; col < mapSize; col++) {
				if(count == 0) {
					count++;
					continue;
				}
				if(gameMap[row][col] == gameMap[row][col - 1] && col != mapSize - 1) {
					count++;
				}
				else {
					if(col == mapSize - 1) {
						if(gameMap[row][col] == gameMap[row][col - 1]) {
							count++;
						}

						if(count >= 5)
							deleteAllVeggiesOfSameType(gameMapMask, gameMap[row][col]);

						else if(count == 4) {
							for (int fourCol = 0; fourCol < mapSize; fourCol++)
								gameMapMask[row][fourCol] = true;
						}

						else if(count == 3) {
							for (int i = 0; i < 3; i++)
								gameMapMask[row][col - i] = true;
						}
					}
					else {
						if(count >= 5)
							deleteAllVeggiesOfSameType(gameMapMask, gameMap[row][col]);
						else if(count == 4) {
							for (int fourCol = 0; fourCol < mapSize; fourCol++)
								gameMapMask[row][fourCol] = true;
						}
						else if(count == 3) {
							for (int i = 1; i < 4; i++)
								gameMapMask[row][col - i] = true;
						}
					}
					count = 1;
				}

			}
		}
	}

	public void colMask(boolean[][] gameMapMask)
	{
		for (int col = 0; col < mapSize; col++) {
			int count = 0;
			for (int row = 0; row < mapSize; row++) {
				if(count == 0) {
					count++;
					continue;
				}
				if(gameMap[row][col] == gameMap[row - 1][col] && row != mapSize - 1) {
					count++;
				}
				else {
					if(row == mapSize - 1) {
						if(gameMap[row][col] == gameMap[row - 1][col]) {
							count++;
						}

						if(count >= 5)
							deleteAllVeggiesOfSameType(gameMapMask, gameMap[row][col]);

						else if(count == 4) {
							for (int fourRow = 0; fourRow < mapSize; fourRow++)
								gameMapMask[fourRow][col] = true;
						}

						else if(count == 3) {
							for (int i = 0; i < 3; i++)
								gameMapMask[row - i][col] = true;
						}
					}

					else {
						if(count >= 5)
							deleteAllVeggiesOfSameType(gameMapMask, gameMap[row][col]);
						else if(count == 4) {
							for (int fourRow = 0; fourRow < mapSize; fourRow++)
								gameMapMask[fourRow][col] = true;
						}
						else if(count == 3) {
							for (int i = 1; i < 4; i++)
								gameMapMask[row - i][col] = true;
						}
					}
					count = 1;
				}

			}
		}
	}

	public boolean anytingElseToDelete()
	{
		for (int x = 0; x < mapSize; x++) {
			for (int y = 0; y < mapSize; y++) {
				if(crossTest(x, y))
					return true;
			}
		}
		return false;
	}

	public void shuffle()
	{
		Random random = new Random();

		for (int i = gameMap.length - 1; i > 0; i--) {
			for (int j = gameMap[i].length - 1; j > 0; j--) {
				int m = random.nextInt(i + 1);
				int n = random.nextInt(j + 1);

				Cell temp = gameMap[i][j];
				gameMap[i][j] = gameMap[m][n];
				gameMap[m][n] = temp;
			}
		}

	}

	public boolean isStepOnTable()
	{
		boolean canStep = false;
		for (int row = 0; row < mapSize; row++) {
			for (int col = 0; col < mapSize; col++) {
				if(crossTest(row, col))
					canStep = true;
				else {
					shuffle();
					canStep = false;
				}
			}
		}
		return canStep;
	}

	public void reworkGameMap()
	{

		Random rand = new Random();

		for (int row = 0; row < mapSize; row++) {
			for (int col = 0; col < mapSize; col++) {
				if(gameMap[row][col].equals(Cell.EMPTY)) {
					gameMap[row][col] = Cell.values()[rand.nextInt(5) + 1];
				}
			}
		}

	}

//	public void del()
//	{
//		for (int row = 0; row < mapSize; row++) {
//			for (int col = 0; col < mapSize; col++) {
//				if(gameMapMask[row][col]) {
//					gameMap[row][col] = Cell.EMPTY;
//					gameMapMask[row][col] = false;
//
//				}
//			}
//		}
//	}

	public boolean delete()
	{

		boolean wasDeleted = false;
		rowMask(gameMapMask);
		colMask(gameMapMask);

		for (int row = 0; row < mapSize; row++) {
			for (int col = 0; col < mapSize; col++) {
				if(gameMapMask[row][col]) {
					score++;
					wasDeleted = true;
				}
			}
		}
		for (int row = 0; row < mapSize; row++) {
			for (int col = 0; col < mapSize; col++) {
				if(gameMapMask[row][col]) {
					gameMap[row][col] = Cell.EMPTY;
					gameMapMask[row][col] = false;

				}
			}
		}

		return wasDeleted;
	}

	public void update()
	{
		while (delete()) {
			reworkGameMap();
		}
	}

	public Cell[][] getGameMap()
	{
		return gameMap;
	}

	public int getHp()
	{
		return hp;
	}

	public int getScore()
	{
		return score;
	}

	public int getMinPoint()
	{
		return minPoint;
	}

	public void setMinPoint(int minPoint)
	{
		this.minPoint = minPoint;
	}

	public void setHp(int hp)
	{
		this.hp = hp;
	}

}
