package MemoryGame;

import Config.Config;
import Messages.Messages;
import UserDao.UserDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

    public class MemoryGame {
        private List<Card> cardList;
        Card[][] board;
        int numMatches;
        private int level;
        int maxTries;
        private int remainingTries;
        private int lastRow;
        private int lastCol;
        private int totalScore;

        public MemoryGame(int level) {
            this.cardList = new ArrayList<>();
            this.level = level;
            if (level == 1) {
                board = new Card[4][5]; // Level 1 board size: 4x5
                maxTries = 10; // Level 1 maximum tries: 10
            } else if (level == 2) {
                board = new Card[4][6]; // Level 2 board size: 4x6
                maxTries = 12; // Level 2 maximum tries: 12
            } else if (level == 3) {
                board = new Card[6][6]; // Level 3 board size: 6x6
                maxTries = 15; // Level 3 maximum tries: 15
            }
            remainingTries = maxTries;
            numMatches = 0;
            lastRow = -1;
            lastCol = -1;
            totalScore = 0; // Initialize total score as 0
        }

        public void initializeBoard() {
            List<Integer> rowIndices = new ArrayList<>();
            for (int row = 0; row < board.length; row++) {
                rowIndices.add(row);
            }
            Collections.shuffle(rowIndices);

            for (int value = 1; value <= (board.length * board[0].length) / 2; value++) {
                cardList.add(new Card(value));
                cardList.add(new Card(value));
            }

            Collections.shuffle(cardList);

            int cardIndex = 0; // Keep track of the next card index to add

            for (int rowIndex : rowIndices) {
                for (int col = 0; col < board[rowIndex].length; col++) {
                    if (cardIndex < cardList.size()) {
                        board[rowIndex][col] = cardList.get(cardIndex); // Assign the card to the board
                        cardIndex++; // Move to the next card
                    } else {
                        // All cards have been assigned, perform any additional action here if needed
                    }
                }
            }
        }

        public void printBoard() {
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    Card card = board[row][col];
                    if (card.isRevealed()) {
                        System.out.print(card + " "); // Print the card at the current position
                    } else {
                        System.out.print("X "); // Hide the card if it hasn't been guessed yet
                    }
                }
                System.out.println(); // Move to a new line after each row
            }
        }

        public boolean playTurn(int row, int col) {
            if (!isValidPosition(row, col)) {
                System.out.println("Invalid position. Please try again.");
                return false;
            }

            Card card = board[row][col];

            if (card.isRevealed()) {
                System.out.println("This card has already been guessed. Please try again.");
                return true;
            }

            card.reveal();
            printBoard();

            if (lastRow != -1 && lastCol != -1) {
                Card lastCard = board[lastRow][lastCol];

                if (card.getValue() == lastCard.getValue() && (row != lastRow || col != lastCol)) {
                    System.out.println("Match found! You earn 2 points.");
                    card.setMatched();
                    lastCard.setMatched();
                    numMatches += 2; // Update numMatches when a match is found
                } else {
                    System.out.println("No match. Try again.");
                    card.hide();
                    lastCard.hide();
                }

                lastRow = -1;
                lastCol = -1;
                remainingTries--; // Decrease remaining tries by 1
            } else {
                lastRow = row;
                lastCol = col;
            }

            System.out.println("Remaining tries: " + remainingTries);
            if (numMatches == cardList.size() / 2) {
                System.out.println("Congratulations! You found all the matches.");
                totalScore = calculateTotalScore();
                return false;
            }

            if (remainingTries <= 0) {
                System.out.println("Game over! You have reached the maximum number of tries.");
                totalScore = calculateTotalScore();
                return false;
            }
            return true;
        }
        int calculateTotalScore() {
            return (numMatches * 1) + (remainingTries * 10);
        }
        private boolean isValidPosition(int row, int col) {
            return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
        }

        public boolean isGameOver() {
            return remainingTries <= 0;
        }

        public int getTotalScore() {
            return totalScore;
        }

        public void saveScoreToDatabase(int score){
            UserDao userDao = new UserDao();
            Config config = new Config();
            Messages message = new Messages();
            // Query
            String query = "INSERT INTO scores (score, user_id) VALUES (?,?)";

            try(Connection connection = DriverManager.getConnection(config.DB_URL, config.USER, config.PASSWORD)) {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, score);
                statement.setInt(2,userDao.getUserId());
                statement.executeUpdate();
                System.out.println("Score saved to the database: " + score);
            } catch (SQLException e) {
                message.defaultErrorMessage(e.getMessage());
//                System.out.println("Error saving score to the database: " + e.getMessage());
            }
        }
    }
    
