package MemoryGame;

public class Card {
        private int  value;
        private boolean revealed;
        private boolean matched;

        public Card(int value) {
            this.value = value;
            this.revealed = false;
            this.matched = false;
        }

        public int getValue() {
            return value;
        }

        public boolean isRevealed() {
            return revealed;
        }

        public boolean isMatched() {
            return matched;
        }

        public void reveal() {
            revealed = true;
        }

        public void hide() {
            revealed = false;
        }

        public void setMatched() {
            matched = true;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }


