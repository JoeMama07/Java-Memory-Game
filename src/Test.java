import java.util.Scanner;

public class Test {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to Memory Game!");

            while (true) {
                System.out.println("Choose a level: (1) Easy, (2) Medium, (3) Hard, or (0) Quit");
                int level = scanner.nextInt();

                if (level == 0) {
                    System.out.println("Thank you for playing. Goodbye!");
                    break;
                }

                if (level < 1 || level > 3) {
                    System.out.println("Invalid level. Please try again.");
                    continue;
                }

                MemoryGame memoryGame = new MemoryGame(level);
                memoryGame.initializeBoard();
                memoryGame.printBoard();

                int remainingTries = memoryGame.maxTries;

                while (remainingTries > 0 && memoryGame.numMatches < (memoryGame.board.length * memoryGame.board[0].length) / 2) {
                    System.out.println("Guess a card (row column): ");
                    int row = scanner.nextInt() - 1; // Subtract 1 to adjust for 0-based indexing
                    int col = scanner.nextInt() - 1; // Subtract 1 to adjust for 0-based indexing

                    if (memoryGame.playTurn(row, col)) {
                        remainingTries--;
                    }
                }

                if (memoryGame.numMatches == (memoryGame.board.length * memoryGame.board[0].length) / 2) {
                    System.out.println("Congratulations! You have matched all pairs within " + memoryGame.maxTries + " tries.");
                } else {
                    System.out.println("Game over. You did not match all pairs within " + memoryGame.maxTries + " tries.");
                }
            }
        }
    }


