// Madiha, Hana, Laura, and Samia
// Intro to CS Final Project
// Minesweeper in Java
// Spring 2026

import java.util.Random;
import java.util.Scanner;

public class Minesweeper{
    public static void main (String[] args){
    	int [][] matrix = new int [5][5];
		
		int valueOfBomb = 9;
		int numberOfBombs = 5;
		Random rand = new Random();
		
		
		//placing bombs in random spots
		int placedBombs = 0;
		while (placedBombs < numberOfBombs) {
			int r = rand.nextInt(5);
			int c = rand.nextInt(5);
			
			//only place a bomb if there isn't one there already
			if (matrix[r][c] != valueOfBomb) {
				matrix[r][c] = valueOfBomb;
				placedBombs ++ ;
			}
		}
		
		int i = 0; int j = 0; int row = 0; int column = 0; 
		//counter for how many bombs our tile is touching!!!
		for (i = 0; i < 5; i++) {
			
			for (j = 0; j < 5; j++) {
				int thenumber = 0;
				if (matrix[i][j] != valueOfBomb) {
					for (row = Math.max(0,i-1); row <= Math.min(4, i+1); row++) {
						for (column = Math.max(0,j-1); column <= Math.min(4, j+1); column++) {
							if (matrix[row][column] == valueOfBomb) {
								thenumber++;
							}
						}			
					}
				matrix[i][j] = thenumber;
				}
			}
		}
		
		//printing the initial board
		int k = 0;
		int l = 0;
		
		for (k = 0; k < 5; k++) {
			for(l = 0; l < 5; l++) {
			System.out.print("-" + " ");
			}
		//new line for each row	
		System.out.println();
		}
		
		//second matrix that keeps track of which cells are revealed
		boolean revealed[][] = new boolean [5][5];

		//playing loop!
		boolean gameOver = false;
		while (gameOver == false) {	
			
			//ask for user input to reveal a cell
			System.out.print("Enter row and column to reveal cell (only numbers 0-4, separated by a space):");
	        Scanner scanner = new Scanner(System.in);
			int r = scanner.nextInt();
			int c = scanner.nextInt();
			
<<<<<<< HEAD
			revealed [r][c] = true;
			
			//checking for bomb here
			if (matrix[r][c] == valueOfBomb) {
				System.out.println("GAME OVER! You lose ˙◠˙");
				gameOver = true;
			}
			
=======
			//explanation of reveal method is in comments within the method
			reveal(matrix, revealed, r, c);
	
>>>>>>> f70750ef54dbc06a187afcb2aa5f5ec96f3524b5
			//prints contents of the cell if it has been revealed, dashes if it hasn't
			//this code generates the display the player sees, a new one is printed with each new input
			for (int m = 0; m < 5; m++) {
				for(int n = 0; n < 5; n++) {

					if (revealed [m][n] == false) {
						System.out.print("-" + " ");
					}
					if (revealed [m][n] == true) {
						System.out.print(matrix[m][n] + " ");
					}		
				}
			//new line for each row	
			System.out.println();
			}			
		}
    }
    public static void reveal (int[][] matrix, boolean[][]revealed, int r, int c) {
    	
    	//this line is the "base case", prevents infinite loop when checking neighbors,
    	//and drops us back off in the playing loop if user enters coordinates for an already revealed cell
    	if (revealed [r][c] == true) return;
    	
    	//if the cell wasn't already revealed, this line marks the chosen cell "revealed"
    	revealed [r][c] = true;
    	
		//if the newly revealed cell is a 0, this code reveals all the neighbors
		if (matrix [r][c] == 0 && revealed [r][c] == true) {
			for (int row = Math.max(0,r-1); row <= Math.min(4, r+1); row++) {
				for (int column = Math.max(0,c-1); column <= Math.min(4, c+1); column++) {
					//this line calls the reveal function on itself, meaning:
					//1) it checks if the neighbor we're looking has already been revealed, and if it has, goes to the next one
					//2) if it hasn't been revealed, marks it as revealed
					//3) if we're just marking it as revealed no and it's a 0, checks the neighbors
					reveal(matrix, revealed, row, column); 
				}
			}
		}
    }
}