package healthy.crush.model;

import java.util.Arrays;
import java.util.Random;

/** This class contains the core logic of the game.
 * 
 * <p>
 * The whole point of this class is to store and later use the data through
 * setters and getters, allowing them to be changed.
 * </p>
 * 
 * @author Georgina Kovács */

public class Game
{
	Cell[][]	gameMap;
	int			score;
	int			hp;
	int			mapSize;
	int			minPoint;
	boolean[][]	gameMapMask;

	/**
	 * Creates a new instance of the model and initializes it.
	 *
	 */
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
	 * This function creates at least one step on the map at the beginning of the game, thus preventing the player from loosing at the very beginning.
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
	 * This function generates a random game map, that has Cell vaues in it.
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

	/**
	 * This function returns if there is at least 2 other with the same type as the one we're checking. It also checks the coordinates so it won't go out of the map.
	 * 
	 * @param x coordinate of a row
	 * @param y coordinate of a column
	 * @return <b>true</b>, if the there are at least 3 of the same type next to each other, <b>false</b> otherwise
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
	 * Swaps the Cell values of the koordinates.
	 * 
	 * @param fromX row of the starter coordinate
	 * @param fromY colummn of the starter coordinate
	 * @param toX row of the goal coordinate
	 * @param toY column of the goal coordinate
	 */
	public void swap(int fromX, int fromY, int toX, int toY)
	{
		Cell temp = gameMap[fromX][fromY];
		gameMap[fromX][fromY] = gameMap[toX][toY];
		gameMap[toX][toY] = temp;
	}

	/**
	 * Checks, if the ones we want to swap are next to each other or not, and checks if there are ate least 2 other of the same type next to each other after swapping them.
	 * 
	 * @param fromX row of the starter coordinate
	 * @param fromY colummn of the starter coordinate
	 * @param toX row of the goal coordinate
	 * @param toY column of the goal coordinate
	 * @return <b>true</b>, if they are next to one another and at least 3 of the same type are in a row after swap, <b>false</b> otherwise
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

	/**
	 * If we can use the {@link Game#canSwapFromTo(int, int, int, int)}. If it's able to swap, it does, and the player looses a Health Point.
	 * 
	 * @param fromX row of the starter coordinate
	 * @param fromY colummn of the starter coordinate
	 * @param toX row of the goal coordinate
	 * @param toY column of the goal coordinate
	 * @return <b>true</b>, if the player could step, <b>false</b> otherwise
	 */
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
	 * Checks, where are the other vegetables that has the same type, and changes the value of the mask to true
	 * 
	 * @param type a {@link Cell} type
	 */
	public void deleteAllVeggiesOfSameType(Cell type)
	{
		for (int row = 0; row < mapSize; row++)
			for (int col = 0; col < mapSize; col++) {
				if(gameMap[row][col].equals(type))
					gameMapMask[row][col] = true;
			}

	}

	/**
	 * Checks the rows. If count of the same type is greater than 5, it uses the {@link Game#deleteAllVeggiesOfSameType(Cell)} function,
	 * if the count is 4, it changes the value of the mask to true in the given row,
	 * if the count is 3, it changes the mask to true on the given cells.
	 * 
	 */
	public void rowMask()
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
							deleteAllVeggiesOfSameType(gameMap[row][col]);

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
							deleteAllVeggiesOfSameType(gameMap[row][col]);
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

	/**
	 * Checks the columns. If count of the same type is greater than 5, it uses the {@link Game#deleteAllVeggiesOfSameType(Cell)} function,
	 * if the count is 4, it changes the value of the mask to true in the given column,
	 * if the count is 3, it changes the mask to true on the given cells.
	 * 
	 */
	public void colMask()
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
							deleteAllVeggiesOfSameType(gameMap[row][col]);

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
							deleteAllVeggiesOfSameType(gameMap[row][col]);
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

	/**
	 * The function calls the {@link Game#rowMask()} and the {@link Game#colMask()} methods, then counts the true values in the mask and adds it to the score.
	 * It changes the cells with the true values of the mask to {@link Cell#EMPTY} and changes the mask back to false.
	 * 
	 * @return <b>true</b> if the map was deleted, <b>false</b> if not
	 */

	public boolean delete()
	{

		boolean wasDeleted = false;
		rowMask();
		colMask();

		for (int row = 0; row < mapSize; row++) {
			for (int col = 0; col < mapSize; col++) {
				if(gameMapMask[row][col]) {
					score++;
				}
			}
		}
		for (int row = 0; row < mapSize; row++) {
			for (int col = 0; col < mapSize; col++) {
				if(gameMapMask[row][col]) {
					gameMap[row][col] = Cell.EMPTY;
					gameMapMask[row][col] = false;
					wasDeleted = true;
				}
			}
		}

		return wasDeleted;
	}

	/**
	 * This function gives the {@link Cell#EMPTY} type cells a randomly generated Cell value.
	 */
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

	/**
	 * This method keeps reworking the game map until there is nothing else to delete anymore.
	 */
	public void update()
	{
		while (delete()) {
			reworkGameMap();
		}
	}

	/**
	 * Returns a {@link Cell} type game map.
	 * 
	 * @return  a {@link Cell} type game map
	 */
	public Cell[][] getGameMap()
	{
		return gameMap;
	}

	/**
	 * Returns the health of the player.
	 * 
	 * @return hp
	 */
	public int getHp()
	{
		return hp;
	}

	/**
	 * Returns the score of the player.
	 * 
	 * @return score
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * Sets the score.
	 * 
	 * @param score score
	 */
	public void setScore(int score)
	{
		this.score = score;
	}

	/**
	 * Returns the minimum points to win the game.
	 * 
	 * @return minPoint
	 */
	public int getMinPoint()
	{
		return minPoint;
	}

	/**
	 * Sets the players Health Point.
	 * 
	 * @param hp hp
	 */
	public void setHp(int hp)
	{
		this.hp = hp;
	}

	/**
	 * Returns the mask of the game map.
	 * @return the mask of the game map
	 */

	public boolean[][] getGameMapMask()
	{
		return gameMapMask;
	}

}
