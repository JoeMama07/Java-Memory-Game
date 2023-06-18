package MemoryGame;

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
            return true;
        }

        private boolean isValidPosition(int row, int col) {
            return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
        }
    }
    