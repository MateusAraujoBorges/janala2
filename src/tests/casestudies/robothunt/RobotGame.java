package tests.casestudies.robothunt;

import janala.Main;

public class RobotGame {

	static final int LEFT = 0;
	static final int RIGHT = 1;
	static final int DOWN = 2;
	static final int UP = 3;

	static final int ROBOT_1 = 0;
	static final int ROBOT_2 = 2;

	static int MOVE_STRENGTH = 1;
	static int X_DRIFT = 0;
	static int Y_DRIFT = 0;

	static int robot1X = 0;
	static int robot1Y = 0;
	static int robot2X = 4;
	static int robot2Y = 4;

	static int map[][] = getMap();

	static final int numMovesPerRobot = 10;
	static int[] robot1Plan = new int[numMovesPerRobot];
	static int[] robot2Plan = new int[numMovesPerRobot];
	static int[] movingToken = new int[2 * numMovesPerRobot];

	static int remainingMovesRobot1 = numMovesPerRobot;
	static int remainingMovesRobot2 = numMovesPerRobot;

	public static void main(String[] args) {
		int x = Main.readInt(0);
        Main.MakeSymbolic(x);
		int y = Main.readInt(0);
        Main.MakeSymbolic(y);
		int z = Main.readInt(0);
        Main.MakeSymbolic(z);
        
		boolean outcome = play(x, y, z);
		System.out.println(outcome);
		printThePlan();

		System.out.println("DONE");
	}

	private static void printThePlan() {
		int robot1NextMove = 0;
		int robot2NextMove = 0;
		for (int token = 0; token < 2 * numMovesPerRobot - remainingMovesRobot1
				- remainingMovesRobot2; token++) {
			if (token == ROBOT_1) {
				System.out.println("R1: " + robot1Plan[robot1NextMove]);
				robot1NextMove++;
			} else {
				System.out.println("R2: " + robot2Plan[robot2NextMove]);
				robot2NextMove++;
			}
		}
	}

	public static int[][] getMap() {
		// Small toy map, just for testing. Use the MapGenerator for a more
		// complex one
		int[][] map = new int[5][5];
		map[1][0] = TerrainGenerator.WATER;
		map[1][1] = TerrainGenerator.WATER;
		map[1][2] = TerrainGenerator.WATER;
		map[3][2] = TerrainGenerator.MOUNTAIN;
		map[3][3] = TerrainGenerator.MOUNTAIN;
		map[3][4] = TerrainGenerator.MOUNTAIN;
		return map;
	}

	public static boolean play(int strength, int xDrift, int yDrift) {
		MOVE_STRENGTH = strength;
		X_DRIFT = xDrift;
		Y_DRIFT = yDrift;
		int moveCounter = 0;
		while (remainingMovesRobot1 > 0 || remainingMovesRobot2 > 0) {
			System.out.println("Move: " + (moveCounter++));
			int nextRobotToMove = pickRobot();
			int nextMove = pickMove(nextRobotToMove);
			boolean robotIsAlive = executeMove(nextRobotToMove, nextMove);
			if (!robotIsAlive) {
				return false;
			}
			if (robot1X == robot2X && robot1Y == robot2Y) {
				System.out.println("VICTORY");
				return true;
			}
		}
		assert false;
		return false;
	}

	private static boolean executeMove(int nextRobotToMove, int nextMove) {
		// Updating plan history
		movingToken[2 * numMovesPerRobot - remainingMovesRobot1
				- remainingMovesRobot2] = nextRobotToMove;
		if (nextRobotToMove == ROBOT_1) {
			robot1Plan[numMovesPerRobot - remainingMovesRobot1] = nextMove;
			remainingMovesRobot1--;
		} else {
			robot2Plan[numMovesPerRobot - remainingMovesRobot2] = nextMove;
			remainingMovesRobot2--;
		}

		// Computing initial position
		int x, y;
		if (nextRobotToMove == ROBOT_1) {
			x = robot1X;
			y = robot1Y;
		} else {
			x = robot2X;
			y = robot2Y;
		}

		// Computing expected move
		int targetX = x;
		int targetY = y;
		switch (nextMove) {
		case LEFT:
			targetX -= MOVE_STRENGTH;
			break;
		case RIGHT:
			targetX += MOVE_STRENGTH;
			break;
		case DOWN:
			targetY -= MOVE_STRENGTH;
			break;
		case UP:
			targetY += MOVE_STRENGTH;
			break;
		}

		// Adding drifting effect
		targetX = targetX + X_DRIFT;
		targetY = targetY + Y_DRIFT;

		// Enforcing valid moves
		targetX = Math.min(Math.max(targetX, 0), map.length - 1);
		targetY = Math.min(Math.max(targetY, 0), map[0].length - 1);

		// Enforce the move, up to the limitations due to the terrain
		// First move along x, then along y
		while (x != targetX) {
			int direction = (x < targetX) ? +1 : -1;
			if (map[x + direction][y] == TerrainGenerator.TERRAIN) {
				x = x + direction;
			} else if (map[x + direction][y] == TerrainGenerator.WATER) {
				// the robot is dead
				assert false;
				return false;
			} else {
				// cannot cross the mountains
				break;
			}
		}

		while (y != targetY) {
			int direction = (y < targetY) ? +1 : -1;
			if (map[x][y + direction] == TerrainGenerator.TERRAIN) {
				y = y + direction;
			} else if (map[x][y + direction] == TerrainGenerator.WATER) {
				// the robot is dead
				assert false;
				return false;
			} else {
				// cannot cross the mountains
				break;
			}
		}

		// Updating global position information
		if (nextRobotToMove == ROBOT_1) {
			robot1X = x;
			robot1Y = y;
		} else {
			robot2X = x;
			robot2Y = y;
		}

		return true;
	}

	private static int pickMove(int nextRobotToMove) {
		int x, y;
		if (nextRobotToMove == ROBOT_1) {
			x = robot1X;
			y = robot1Y;
		} else {
			x = robot2X;
			y = robot2Y;
		}

//		int[] allowedMoves = new int[4];
		//janala doesn't like non-initialized cells in arrays
		int[] allowedMoves = new int[]{0,0,0,0};
		
		
		int allowedMovesCounter = -1;

		// The robot looks only one step ahead
		if (x > 0 && map[x - 1][y] == TerrainGenerator.TERRAIN) {
			allowedMovesCounter++;
			allowedMoves[allowedMovesCounter] = LEFT;
		}
		if (x < map.length - 1 && map[x + 1][y] == TerrainGenerator.TERRAIN) {
			allowedMovesCounter++;
			allowedMoves[allowedMovesCounter] = RIGHT;
		}
		if (y > 0 && map[x][y - 1] == TerrainGenerator.TERRAIN) {
			allowedMovesCounter++;
			allowedMoves[allowedMovesCounter] = DOWN;
		}
		if (y < map[x].length - 1 && map[x][y + 1] == TerrainGenerator.TERRAIN) {
			allowedMovesCounter++;
			allowedMoves[allowedMovesCounter] = UP;
		}

		if (allowedMovesCounter < 0) {
			// this should never occur on a well deisgned game
			assert false;
		}

//		int moveSelector = Verify.getInt(0, allowedMovesCounter);
		int moveSelector = Main.readInt(0);
		Main.MakeSymbolic(moveSelector,0,3);
		
		return allowedMoves[moveSelector];
	}

	private static int pickRobot() {
		int robot = -1;
		if (remainingMovesRobot1 > 0) {
			if (remainingMovesRobot2 > 0) {
//				robot = Verify.getInt(0, 1);
				robot = Main.readInt(0);
				Main.MakeSymbolic(robot,0,1);
			} else {
				robot = ROBOT_1;
			}
		} else {
			if (remainingMovesRobot2 > 0) {
				robot = ROBOT_2;
			}
		}
		return robot;
	}

	/*
	 * 
	 * public static MOVE pickMove(int x, int y) { int choice; // check if we
	 * are in a corner? if (x == 0 && y == 0) { // bottom left choice =
	 * Verify.getInt(0, 1); if (choice == 0) return MOVE.up; else return
	 * MOVE.right; } if (x == 0 && y == rowsMax) { // top left choice =
	 * Verify.getInt(0, 1); if (choice == 0) return MOVE.down; else return
	 * MOVE.right; } if (x == colsMax && y == 0) { // bottom right choice =
	 * Verify.getInt(0, 1); if (choice == 0) return MOVE.up; else return
	 * MOVE.left; } if (x == colsMax && y == rowsMax) { // top right choice =
	 * Verify.getInt(0, 1); if (choice == 0) return MOVE.down; else return
	 * MOVE.left; }
	 * 
	 * if (x == 0) { // left edge but not in a corner choice = Verify.getInt(0,
	 * 2); if (choice == 0) return MOVE.down; else if (choice == 1) return
	 * MOVE.up; else return MOVE.right; }
	 * 
	 * if (y == rowsMax) { // top edge but not in a corner choice =
	 * Verify.getInt(0, 2); if (choice == 0) return MOVE.down; else if (choice
	 * == 1) return MOVE.left; else return MOVE.right; }
	 * 
	 * if (x == colsMax) { // right edge but not in a corner choice =
	 * Verify.getInt(0, 2); if (choice == 0) return MOVE.down; else if (choice
	 * == 1) return MOVE.up; else return MOVE.left; }
	 * 
	 * if (y == 0) { // bottom edge but not in a corner choice =
	 * Verify.getInt(0, 2); if (choice == 0) return MOVE.left; else if (choice
	 * == 1) return MOVE.right; else return MOVE.up; } // internal move so
	 * everything is valid choice = Verify.getInt(0, 3); if (choice == 0) return
	 * MOVE.left; else if (choice == 1) return MOVE.right; else if (choice == 2)
	 * return MOVE.up; else return MOVE.down; }
	 * 
	 * public static boolean play(int movesMax, int weight) { //
	 * System.out.println("HERE!!!!!!!!"); int moveCounter = 0; int currentCol =
	 * startCol; int currentRow = startRow; MOVE move;
	 * 
	 * while (moveCounter < movesMax) { // adjust for the wind int adjustment =
	 * wind[currentCol]; if (weight < 30) { adjustment += 0; // no adjustment }
	 * else { adjustment -= 1; // too heavy to be moved as much }
	 * 
	 * currentRow += adjustment; if (currentRow > rowsMax) currentRow = rowsMax;
	 * // pick a legal move move = pickMove(currentCol, currentRow);
	 * moves[moveCounter] = move; // what is the resulting position if (move ==
	 * MOVE.up) { currentRow++; } else if (move == MOVE.down) { currentRow--; }
	 * else if (move == MOVE.left) { currentCol--; } else { currentCol++; } //
	 * System.out.println("(" + currentRow + "," + currentCol + ")" );
	 * 
	 * // System.out.println("After wind = (" + currentRow + "," + currentCol +
	 * // ")" );
	 * 
	 * if (currentCol == goalCol && currentRow == goalRow) return true;
	 * moveCounter++; } return false; }
	 * 
	 * public static void main(String[] args) { wind[0] = 0; wind[1] = 0;
	 * wind[2] = 0; wind[3] = 1; wind[4] = 1; wind[5] = 1; wind[6] = 2; wind[7]
	 * = 2; wind[8] = 1; wind[9] = 0; boolean goalReached = play(numMoves,50);
	 * if (goalReached) { System.out.println("Found it? " + goalReached); for
	 * (MOVE m : moves) { System.out.println(m); } assert false; } }
	 */
}
