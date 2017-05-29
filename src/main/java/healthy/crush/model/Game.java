package healthy.crush.model;

import java.util.Arrays;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/* Ez az osztály adja meg az alap játéklogikát. */

public class Game
{
	Cell[][]			gameMap;
	int					score;
	int					hp;
	int					mapSize;
	int					minPoint;
	private Timeline	timer;
	boolean[][]			gameMapMask;

	public Game(int mapSize, int hp, int minPoint)
	{
		gameMap = new Cell[mapSize][mapSize];

		gameMapMask = new boolean[mapSize][mapSize];
		for (Cell[] row : gameMap)
			Arrays.fill(row, Cell.EMPTY);

		// this.score = score;

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
		int minx = (x - 2) < 0 ? 0 : x - 2;
		int maxx = (x + 2) > mapSize - 1 ? mapSize - 1 : x + 2;
		int miny = (y - 2) < 0 ? 0 : y - 2;
		int maxy = (y + 2) > mapSize - 1 ? mapSize - 1 : y + 2;
		int count = 0;
		Cell tempc = Cell.EMPTY;
		for (int i = minx; i <= maxx; i++) {
			if(tempc.equals(gameMap[i][y])) {
				count++;
			}
			else {
				count = 1;
				tempc = gameMap[i][y];
			}
			if(count >= 3)
				return true;
		}

		count = 0;
		tempc = Cell.EMPTY;
		for (int i = miny; i <= maxy; i++) {
			if(count >= 3)
				return true;
			if(tempc.equals(gameMap[x][i])) {
				count++;
			}
			else {
				count = 1;
				tempc = gameMap[x][i];
			}
			if(count >= 3)
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
		if(((fromX == toX || (fromY == toY)) && (Math.abs(fromX - toX) < 2 && Math.abs(fromY - toY) < 2))) {

			swap(fromX, fromY, toX, toY);

			if(crossTest(toX, toY) || crossTest(fromX, fromY)) {
				canSwap = true;
			}
			else
				canSwap = false;
			swap(toX, toY, fromX, fromY);
		}
		return canSwap;

	}

	public boolean step(int fromX, int fromY, int toX, int toY)
	{
		if(canSwapFromTo(fromX, fromY, toX, toY)) {
			swap(fromX, fromY, toX, toY);
			hp--;
			return true;
		}

		return false;
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

	public void setScore(int score)
	{
		this.score = score;
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
