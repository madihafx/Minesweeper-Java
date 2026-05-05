// Madiha, Hana, Laura, and Samia
// Intro to CS Final Project
// Minesweeper in Java
// Spring 2026

import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

    //Tester 
    
    // In-game symbols
    static final String HIDDEN = "[🔒]"; // cell not yet revealed
    static final String BOMB = "[💣]"; // bomb revealed after game over
    static final String EMPTY = "[🛡️]"; // revealed cell with 0 neighboring bombs
    static final String FLAG = "[🚩]"; // player-flagged cell

    // Board min and max sizes
    static final int MIN_SIZE = 2;
    static final int MAX_SIZE = 20;

    // Main 
    public static void main(String[] args)
            throws java.lang.InterruptedException {
        {
            Scanner sc = new Scanner(System.in);

            // Print banner and instructions on game start
            printInstructions();
            Thread.sleep(1000);

            // Boolean for looping through game
            boolean playAgain = true;

            // Keep playing while user wants to
            while (playAgain) {

                // Use helper method to handle size input and logic
                int size = chooseBoardSize(sc);

                // Use helper method to handle difficulty input and logic
                int numberOfBombs = chooseBombCount(sc, size);

                // Value for bombs for logic purposes
                int valueOfBomb = 99;

                // Initialize 2D array
                int[][] matrix = new int[size][size];

                // Initialize 2D array for if the player has revealed the spot
                boolean[][] revealed = new boolean[size][size];

                // Initialize 2D array for if the player has flagged the spot
                boolean[][] flagged = new boolean[size][size];

                // move counter
                int moves = 0;

                // Boolean for first move, to make sure 1st move is always free of bombs
                boolean firstMove = true;

                // the last letter column label
                char lastCol = (char) ('A' + size - 1);

                System.out.println("\nBoard: " + size + "x" + size + " | Bombs: " + numberOfBombs + " | Good luck!");
                System.out.println("  (Your first move is always safe!)\n");
                Thread.sleep(1000);
                printBoard(matrix, revealed, flagged, valueOfBomb, size);

                // State variables
                boolean gameOver = false;
                boolean playerWon = false;

                while (!gameOver) {
                    int flagsPlaced = countFlags(flagged, size);
                    System.out.println("\nBombs remaining: " + (numberOfBombs - flagsPlaced) + "  |  Moves: " + moves);
                    System.out.println("Options:  R = Reveal a cell     F = Flag/unflag a cell");
                    System.out.print("Enter action (R or F): ");
                    String action = getActionInput(sc);

                    // input and validate row and column
                    int r, c;
                    while (true) {
                        System.out.print("Enter row    (1 to " + size + "): ");
                        r = getRowInput(sc, size);

                        System.out.print("Enter column (A to " + lastCol + "): ");
                        c = getColInput(sc, size);

                        // location checks for FLAG
                        if (action.equals("F")) {
                            if (revealed[r][c]) {
                                System.out.println("  That cell is already revealed and cannot be flagged.");
                                System.out.println("  Please choose a different cell.");
                                continue;
                            }
                            break; // valid flag target
                        }

                        // cannot reveal cell twice
                        if (revealed[r][c]) {
                            System.out.println("  That cell is already revealed. Please choose a different cell.");
                            continue;
                        }
                        if (flagged[r][c]) {
                            System.out.println("  That cell is flagged. Unflag it first (F) or pick a different cell.");
                            continue;
                        }
                        break; // valid reveal target
                    }

                    // execute FLAG action
                    if (action.equals("F")) {
                        if (!flagged[r][c] && countFlags(flagged, size) >= numberOfBombs) {
                            // warn the player if they try to place more flags than there are bombs
                            System.out.println("  Warning: you have already placed " + numberOfBombs
                                    + " flag(s) - equal to the total number of bombs!");
                            System.out.println("  Remove a flag elsewhere before placing a new one.");
                        } else {
                            flagged[r][c] = !flagged[r][c]; // toggle: flag if clear, clear if flagged
                            String status = flagged[r][c] ? "flagged" : "unflagged";
                            // display uses 1-based row and letter column for the confirmation message
                            System.out.println("  Cell " + (r + 1) + (char) ('A' + c) + " " + status + ".");
                            printBoard(matrix, revealed, flagged, valueOfBomb, size);
                        }
                        continue; // flagging doesn't count as a move
                    }

                    moves++; // count this as a move

                    // first move safety
                    if (firstMove) {
                        placeBombs(matrix, numberOfBombs, valueOfBomb, size, r, c);
                        calculateNeighbors(matrix, valueOfBomb, size);
                        firstMove = false;
                    }

                    // if player hits a bomb
                    if (matrix[r][c] == valueOfBomb) {
                        revealed[r][c] = true;
                        System.out.println("\n  💥 BOOM! You hit a bomb after " + moves + " move(s)! Game over... ˙◠˙");
                        Thread.sleep(1000);
                        revealAllBombs(matrix, revealed, valueOfBomb, size);
                        printBoard(matrix, revealed, flagged, valueOfBomb, size);
                        gameOver = true;
                        continue;
                    }

                    // safe cell
                    reveal(matrix, revealed, r, c, valueOfBomb, size);
                    printBoard(matrix, revealed, flagged, valueOfBomb, size);

                    // check if game won
                    if (checkWin(matrix, revealed, valueOfBomb, size)) {
                        // auto-flag any remaining unflagged bombs so the final board looks complete
                        for (int i = 0; i < size; i++) {
                            for (int j = 0; j < size; j++) {
                                if (matrix[i][j] == valueOfBomb && !flagged[i][j]) {
                                    flagged[i][j] = true;
                                }
                            }
                        }
                        printBoard(matrix, revealed, flagged, valueOfBomb, size);
                        System.out.println("  ⭐ You revealed all safe cells in " + moves +
                                " move(s)! You win! (^_^)");
                        Thread.sleep(1000);
                        gameOver = true;
                        playerWon = true;
                    }
                }

                // Game end
                if (playerWon) {
                    System.out.println("Great job! Think you can beat that move count next time?");
                } else {
                    System.out.println("Better luck next time!");
                }

                System.out.print("\nPlay again? (Y = yes, any other key = quit): ");
                String again = sc.nextLine().trim().toUpperCase();
                playAgain = again.equals("Y");

                if (playAgain) {
                    System.out.println("\n--- Starting a new game! ---\n");
                }
            }

            System.out.println("\nThanks for playing Minesweeper! Goodbye!");
            sc.close();
        }
    }

    // prints the welcome banner and how-to-play instructions
    static void printInstructions() throws java.lang.InterruptedException {
        System.out.println("");
        System.out.println("WELCOME TO MINESWEEPER! 💣");
        System.out.println("");
        System.out.println();
        System.out.println("Instructions:");
        Thread.sleep(500);
        System.out.println("  - The board is a grid of hidden cells.");
        Thread.sleep(500);
        System.out.println("  - Some cells hide bombs 💣. Your goal is to");
        Thread.sleep(500);
        System.out.println("    reveal ALL safe cells without hitting a bomb.");
        Thread.sleep(500);
        System.out.println("  - When you reveal a safe cell, it shows a number");
        Thread.sleep(500);
        System.out.println("    indicating how many bombs are touching it.");
        Thread.sleep(500);
        System.out.println("  - A shield [🛡️] means zero bombs are nearby.");
        Thread.sleep(500);
        System.out.println("    Revealing a [🛡️] auto-reveals its neighbors too!");
        Thread.sleep(500);
        System.out.println("  - Use flags 🚩 to mark cells you think are bombs.");
        Thread.sleep(500);
        System.out.println("    Flagged cells cannot be accidentally revealed.");
        Thread.sleep(500);
        System.out.println();
        Thread.sleep(500);
        System.out.println("In-Game Symbols:");
        Thread.sleep(500);
        System.out.println("  [🔒]  => hidden (not yet revealed)");
        Thread.sleep(500);
        System.out.println("  [🛡️]  => revealed, 0 bombs nearby");
        Thread.sleep(500);
        System.out.println("  [1]   => revealed, 1 bomb nearby  (up to [8])");
        Thread.sleep(500);
        System.out.println("  [💣]  => BOMB (shown when game ends)");
        Thread.sleep(500);
        System.out.println("  [🚩]  => flagged by you");
        System.out.println();
        Thread.sleep(1000);
        System.out.println("EXAMPLE 3x3 board (mid-game):");
        System.out.println("      A     B     C");
        System.out.println("  1  [🛡️]  [🛡️]  [1]");
        System.out.println("  2  [🛡️]  [1]   [🔒]");
        System.out.println("  3  [🔒]  [🚩]  [🔒]");
        System.out.println("  (Row 3, Col B is flagged as a suspected bomb.)");
        System.out.println();
        Thread.sleep(2000);

    }

    // handles input and validation of board size
    static int chooseBoardSize(Scanner sc) {
        System.out.println("CHOOSE BOARD SIZE:");
        System.out.println("  Enter any number from " + MIN_SIZE + " to " + MAX_SIZE + ".");
        System.out.println("  (e.g. 5 = tiny, 9 = classic, 16 = large, 20 = massive)");
        System.out.print("Board size: ");

        while (true) {
            try {
                int size = sc.nextInt();
                sc.nextLine(); // consume leftover newline after nextInt()
                if (size >= MIN_SIZE && size <= MAX_SIZE) {
                    System.out.println("  " + size + "x" + size + " board selected!\n");
                    return size;
                }
                System.out.print("  Please enter a number from " + MIN_SIZE + " to " + MAX_SIZE + ": ");
            } catch (java.util.InputMismatchException e) {
                sc.nextLine(); // flush bad input so scanner is ready for next read
                System.out.print("  Not a number. Enter a size from " + MIN_SIZE + " to " + MAX_SIZE + ": ");
            }
        }
    }

    // handles input and validation of difficulty level
    static int chooseBombCount(Scanner sc, int size) {
        // Math.max(1, ...) guarantees at least 1 bomb even on the smallest board
        int easyBombs = Math.max(1, (size * size) / 10); // ~10% of cells
        int mediumBombs = Math.max(1, (size * size) / 5); // ~20% of cells
        int hardBombs = Math.max(1, (size * size) / 3); // ~33% of cells

        System.out.println("CHOOSE DIFFICULTY:");
        System.out.println("  1) Easy   - " + easyBombs + " bomb(s)  (~10% of board)");
        System.out.println("  2) Medium - " + mediumBombs + " bomb(s)  (~20% of board)");
        System.out.println("  3) Hard   - " + hardBombs + " bomb(s)  (~33% of board)");
        System.out.print("Difficulty (1, 2, or 3): ");

        while (true) {
            try {
                int choice = sc.nextInt();
                sc.nextLine(); // consume leftover newline
                if (choice == 1) {
                    System.out.println("  Easy selected!\n");
                    return easyBombs;
                }
                if (choice == 2) {
                    System.out.println("  Medium selected!\n");
                    return mediumBombs;
                }
                if (choice == 3) {
                    System.out.println("  Hard selected!\n");
                    return hardBombs;
                }
                System.out.print("  Invalid. Enter 1, 2, or 3: ");
            } catch (java.util.InputMismatchException e) {
                sc.nextLine(); // flush bad input
                System.out.print("  Not a number. Enter 1, 2, or 3: ");
            }
        }
    }

    // randomly places a bombs according to the difficulty
    static void placeBombs(int[][] matrix, int numberOfBombs, int valueOfBomb,
            int size, int firstR, int firstC) {
        Random rand = new Random();
        int placed = 0;

        while (placed < numberOfBombs) {
            int r = rand.nextInt(size);
            int c = rand.nextInt(size);
            // skip the player's first chosen cell and any cell that already has a bomb
            if ((r == firstR && c == firstC) || matrix[r][c] == valueOfBomb) {
                continue;
            }
            matrix[r][c] = valueOfBomb;
            placed++;
        }
    }

    // fills every non-bomb cell with the count of bombs in its vicinity
    static void calculateNeighbors(int[][] matrix, int valueOfBomb, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] != valueOfBomb) {
                    int count = 0;
                    // check all 8 surrounding cells; Math.max/min clamps to board edges
                    for (int row = Math.max(0, i - 1); row <= Math.min(size - 1, i + 1); row++) {
                        for (int col = Math.max(0, j - 1); col <= Math.min(size - 1, j + 1); col++) {
                            if (matrix[row][col] == valueOfBomb) {
                                count++;
                            }
                        }
                    }
                    matrix[i][j] = count;
                }
            }
        }
    }

    // recursively reveals cells if no bombs close to it
    static void reveal(int[][] matrix, boolean[][] revealed, int r, int c, int valueOfBomb, int size) {
        if (revealed[r][c])
            return; // base case: already revealed, stop here

        revealed[r][c] = true; // mark this cell as revealed

        // if no neighboring bombs, automatically reveal all 8 surrounding cells
        if (matrix[r][c] == 0) {
            for (int row = Math.max(0, r - 1); row <= Math.min(size - 1, r + 1); row++) {
                for (int col = Math.max(0, c - 1); col <= Math.min(size - 1, c + 1); col++) {
                    reveal(matrix, revealed, row, col, valueOfBomb, size);
                }
            }
        }
    }

    // reveals all bombs at game over
    static void revealAllBombs(int[][] matrix, boolean[][] revealed,
            int valueOfBomb, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == valueOfBomb) {
                    revealed[i][j] = true;
                }
            }
        }
    }

    // checks if game is won after each move
    static boolean checkWin(int[][] matrix, boolean[][] revealed,
            int valueOfBomb, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // any unrevealed safe cell means the game is not yet won
                if (matrix[i][j] != valueOfBomb && !revealed[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // counts flags placed for player information
    static int countFlags(boolean[][] flagged, int size) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (flagged[i][j])
                    count++;
            }
        }
        return count;
    }

    // handles input and validation of action (reveal or flag)
    static String getActionInput(Scanner sc) {
        while (true) {
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equals("R") || input.equals("F")) {
                return input;
            }
            System.out.print("  Invalid. Enter R (reveal) or F (flag): ");
        }
    }

    // prompts for a row input
    static int getRowInput(Scanner sc, int size) {
        while (true) {
            try {
                int value = sc.nextInt();
                sc.nextLine(); // consume leftover newline after nextInt()
                if (value >= 1 && value <= size) {
                    return value - 1; // convert to 0-based index for internal use
                }
                System.out.print("  Out of range. Enter a row number from 1 to " + size + ": ");
            } catch (java.util.InputMismatchException e) {
                sc.nextLine(); // flush bad input
                System.out.print("  Not a valid number. Enter a row number from 1 to " + size + ": ");
            }
        }
    }

    // prompts for a column input
    static int getColInput(Scanner sc, int size) {
        char lastCol = (char) ('A' + size - 1);
        while (true) {
            String input = sc.nextLine().trim().toUpperCase();
            // must be exactly one character and a valid letter for this board
            if (input.length() == 1) {
                char letter = input.charAt(0);
                if (letter >= 'A' && letter <= lastCol) {
                    return letter - 'A'; // convert letter to 0-based index for internal use
                }
            }
            System.out.print("  Invalid. Enter a column letter from A to " + lastCol + ": ");
        }
    }

    // prints the current board with all revealed objects
    static void printBoard(int[][] matrix, boolean[][] revealed, boolean[][] flagged,
            int valueOfBomb, int size) {
        System.out.println();

        // column letter headers (A, B, C...) - each cell is 6 characters wide
        System.out.print("      ");
        for (int col = 0; col < size; col++) {
            // (char)('A' + col) converts 0 -> 'A', 1 -> 'B', 2 -> 'C', etc.
            System.out.printf("%-6c", (char) ('A' + col));
        }
        System.out.println();

        // separator line under the column headers
        System.out.print("     ");
        for (int col = 0; col < size; col++) {
            System.out.print("------");
        }
        System.out.println();

        // each row: print a 1-based row number on the left, then each cell
        for (int i = 0; i < size; i++) {
            // (i + 1) converts 0-based index to display number 1, 2, 3...
            System.out.printf("%3d | ", i + 1);

            for (int j = 0; j < size; j++) {
                if (!revealed[i][j] && flagged[i][j]) {
                    System.out.print(FLAG + "   "); // flagged cell
                } else if (!revealed[i][j]) {
                    System.out.print(HIDDEN + "   "); // hidden cell
                } else if (matrix[i][j] == valueOfBomb) {
                    System.out.print(BOMB + "   "); // bomb (shown on game over)
                } else if (matrix[i][j] == 0) {
                    System.out.print(EMPTY + "   "); // safe, no neighboring bombs
                } else {
                    System.out.print("[" + matrix[i][j] + "]   "); // safe, shows neighbor count
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}