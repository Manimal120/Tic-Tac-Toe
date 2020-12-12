import java.util.Scanner;

public class Main {
	static Scanner input = new Scanner(System.in);
	//Define the final variable
	static final int DEPTH = 9; // Depth
	static final int MAN = 1; // Player is 1
	static final int COM = -1; // Computer is -1
	static final int MAX_VALUE = 1000; 
	static final int MIN_VALUE = -1000; 

	static Chess move = new Chess(); // Calculate the computer moves 
	static int[][] board = new int[3][3];
	static int currentPlayer = 0; 
	static int currentDepth = 0; 
	
	//Print the chessboard
	static void printBoard() {
		int i = 0, j = 0;
		for (i = 0; i < 3; i++) {
			System.out.println("-------------");
			for (j = 0; j < 3; j++) {
				if (board[i][j] == MAN) {
					System.out.print("| O ");
				} else if (board[i][j] == COM) {
					System.out.print("| X ");
				} else {
					System.out.print("|   ");
				}
			}
			System.out.println("|");
		}
		System.out.println("-------------");
	}
	
	// Judge win or lose
	static int isWin() {
		int i = 0;
		// Judge the row -
		for (i = 0; i < 3; i++) {
			if (board[i][0] + board[i][1] + board[i][2] == 3)
				return 1;
			else if (board[i][0] + board[i][1] + board[i][2] == -3)
				return -1;
		}
		// Judge the col |
		for (i = 0; i < 3; i++) {
			if (board[0][i] + board[1][i] + board[2][i] == 3)
				return 1;
			else if (board[0][i] + board[1][i] + board[2][i] == -3)
				return -1;
		}
		// Judge the slash, back slash / \
		if (board[0][0] + board[1][1] + board[2][2] == 3)
			return 1;
		else if (board[0][0] + board[1][1] + board[2][2] == -3)
			return -1;
		else if (board[0][2] + board[1][1] + board[2][0] == 3)
			return 1;
		else if (board[0][2] + board[1][1] + board[2][0] == -3)
			return -1;
		return 0;
	}

	// Evaluate the win or lose
	static int evaluate() {
		if (isWin() == COM) // Computer wins
			return MAX_VALUE;
		if (isWin() == MAN) // Human wins
			return MIN_VALUE;
		return 0;
	}
	
	// Player moves
	static void manGo() {
		System.out.print("Please input the location for you to move£º(like£º1 1 is [1,1])");
		int man_x = input.nextInt() - 1;
		int man_y = input.nextInt() - 1;
		while (man_x < 0 || man_x >= 3 || man_y < 0 || man_y >= 3) {
			System.out.print("Wrong location£º");
			man_x = input.nextInt() - 1;
			man_y = input.nextInt() - 1;
		}
		while (board[man_x][man_y] != 0) {
			System.out.print("Here already have one£º");
			man_x = input.nextInt() - 1;
			man_y = input.nextInt() - 1;
		}
		board[man_x][man_y] = MAN;

	}

	// Minimax
	static int miniMax(int depth) {
		int value = 0; // For evaluate
		int bestValue = 0;
		int moveCount = 0;
		int i = 0, j = 0;
		Chess[] moveList = new Chess[9];
		if (isWin() == COM || isWin() == MAN||depth == 0) {
			return evaluate();   
		}
		
		if (currentPlayer == COM) {
			bestValue = MIN_VALUE;
		} else if (currentPlayer == MAN) {
			bestValue = MAX_VALUE;
		}
		
		
		for (i = 0; i < 3; i++) {
			for (j = 0; j < 3; j++) {
				if (board[i][j] == 0) {
					moveList[moveCount] = new Chess();
					moveList[moveCount].x = i;
					moveList[moveCount].y = j;
					moveCount++;
				}
			}
		}
		
		for (i = 0; i < moveCount; i++) {
			Chess currentMove = new Chess();
			currentMove = moveList[i];
			board[currentMove.x][currentMove.y] = currentPlayer;  //Move to the parent location
			currentPlayer= -currentPlayer;
			value = miniMax(depth - 1);
			board[currentMove.x][currentMove.y] = 0;  //clear last operation
			currentPlayer= -currentPlayer;
			if (currentPlayer == COM) {
				if (value > bestValue) {
					bestValue = value;
					if (depth == currentDepth) {
						move = currentMove;  //get the best
					}
				}
			} else if (currentPlayer == MAN) {
				if (value < bestValue) {
					bestValue = value; 
					if (depth == currentDepth) {
						move = currentMove;
					}
				}
			}
		}
		return bestValue;
	}

	// Player first
	static void manFirst() {
		int i = 0;
		currentDepth = 9;
		currentPlayer = MAN;
		for (i = 0; i < 9;) {
			manGo();
			printBoard();
			if (isWin() == currentPlayer) {
				System.out.println("You win!");
				break;
			}
			i++;
			currentDepth--;
			if (i == 9) {
				System.out.println("Draw.");
				break;
			}
			// Then computer
			currentPlayer = COM;
			miniMax(currentDepth);
			board[move.x][move.y] = COM;
			printBoard();
			if (isWin() == currentPlayer) {
				System.out.println("You lose!");
				break;
			}
			i++;
			currentDepth--;
			// Then player
			currentPlayer = MAN;
		}
	}

	static void computerFirst() {
		int i = 0;
		currentDepth = 9;
		currentPlayer = COM;
		for (i = 0; i < 9;) {
			miniMax(currentDepth);
			board[move.x][move.y] = COM;
			printBoard();
			if (isWin() == currentPlayer) {
				System.out.println("You lose!");
				break;
			}
			i++;
			currentDepth--;
			if (i == 9) {
				System.out.println("Draw.");
				break;
			}
			currentPlayer = MAN;
			manGo();
			printBoard();
			if (isWin() == currentPlayer) {
				System.out.println("You win!");
				break;
			}
			i++;
			currentDepth--;
			currentPlayer = COM;
		}
	}

	public static void main(String[] args) {
		// Decide who play first	
		System.out.print("1 is player first, 2 is the computer");
		int turn = input.nextInt();
		if (turn == 1) {
			currentPlayer = 1;
			manFirst();
		} else if (turn == 2) {
			currentPlayer = -1;
			computerFirst();
		} else {
			System.out.println("Wrong choice!");
		}
		input.close();
	}

}

// Define a chess
class Chess {
	protected int x;
	protected int y;

	Chess() {
		this.x = 0;
		this.y = 0;
	}
}