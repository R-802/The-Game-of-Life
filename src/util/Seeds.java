package util;

import static main.TheGame.*;

public class Seeds {
    public void glider() {
        clearCells();
        CELLS[1][2] = true;
        CELLS[2][3] = true;
        CELLS[3][1] = true;
        CELLS[3][2] = true;
        CELLS[3][3] = true;
        drawGame();
    }

    public void LWSS() {
        clearCells();
        CELLS[2][3] = true;
        CELLS[2][4] = true;
        CELLS[3][2] = true;
        CELLS[4][2] = true;
        CELLS[4][5] = true;
        CELLS[5][2] = true;
        CELLS[5][3] = true;
        CELLS[5][4] = true;
        CELLS[5][5] = true;
        drawGame();
    }

    public void HWSS() {
        clearCells();

        int[][] hwssCoords = {
                {2, 3}, {2, 8},
                {3, 2}, {3, 9},
                {4, 2}, {4, 9},
                {5, 2}, {5, 9},
                {6, 2}, {6, 3}, {6, 4}, {6, 5}, {6, 6}, {6, 7}, {6, 8}
        };

        for (int[] coord : hwssCoords) {
            int row = coord[0];
            int col = coord[1];
            CELLS[row][col] = true;
        }

        drawGame();
    }

    public void rand() {
        clearCells();
        for (int row = 0; row < CELLS.length; row++) {
            for (int col = 0; col < CELLS[row].length; col++) {
                CELLS[row][col] = Math.random() >= 0.5;
            }
        }
        drawGame();
    }

    public void GGG() {
        clearCells();

        // Coordinates for the Gosper Glider Gun
        int[][] gunCoords = {
                {1, 25},
                {2, 23}, {2, 25},
                {3, 13}, {3, 14}, {3, 21}, {3, 22}, {3, 35}, {3, 36},
                {4, 12}, {4, 16}, {4, 21}, {4, 22}, {4, 35}, {4, 36},
                {5, 1}, {5, 2}, {5, 11}, {5, 17}, {5, 21}, {5, 22},
                {6, 1}, {6, 2}, {6, 11}, {6, 15}, {6, 17}, {6, 18}, {6, 23}, {6, 25},
                {7, 11}, {7, 17}, {7, 25},
                {8, 12}, {8, 16},
                {9, 13}, {9, 14}
        };


        // Set the cells for the Gosper Glider Gun pattern
        for (int[] coord : gunCoords) {
            int row = coord[0];
            int col = coord[1];
            CELLS[row][col] = true;
        }
        drawGame();
    }

    public void pulsar() {
        clearCells();

        int[][] pulsarCoords = {
                {4, 2}, {4, 3}, {4, 4}, {4, 8}, {4, 9}, {4, 10},
                {6, 2}, {6, 4}, {6, 8}, {6, 10},
                {7, 2}, {7, 4}, {7, 8}, {7, 10},
                {8, 2}, {8, 4}, {8, 8}, {8, 10},
                {9, 2}, {9, 3}, {9, 4}, {9, 8}, {9, 9}, {9, 10},

                {4, 14}, {4, 15}, {4, 16}, {4, 20}, {4, 21}, {4, 22},
                {6, 14}, {6, 16}, {6, 20}, {6, 22},
                {7, 14}, {7, 16}, {7, 20}, {7, 22},
                {8, 14}, {8, 16}, {8, 20}, {8, 22},
                {9, 14}, {9, 15}, {9, 16}, {9, 20}, {9, 21}, {9, 22},

                {14, 2}, {14, 3}, {14, 4}, {14, 8}, {14, 9}, {14, 10},
                {16, 2}, {16, 4}, {16, 8}, {16, 10},
                {17, 2}, {17, 4}, {17, 8}, {17, 10},
                {18, 2}, {18, 4}, {18, 8}, {18, 10},
                {19, 2}, {19, 3}, {19, 4}, {19, 8}, {19, 9}, {19, 10},

                {14, 14}, {14, 15}, {14, 16}, {14, 20}, {14, 21}, {14, 22},
                {16, 14}, {16, 16}, {16, 20}, {16, 22},
                {17, 14}, {17, 16}, {17, 20}, {17, 22},
                {18, 14}, {18, 16}, {18, 20}, {18, 22},
                {19, 14}, {19, 15}, {19, 16}, {19, 20}, {19, 21}, {19, 22},
        };

        for (int[] coord : pulsarCoords) {
            int row = coord[0];
            int col = coord[1];
            CELLS[row][col] = true;
        }

        drawGame();
    }

    public void pentaDecathlon() {
        clearCells();

        int[][] pentaDecathlonCoords = {
                {6, 10}, {6, 11}, {6, 12}, {6, 13},
                {7, 9}, {7, 14},
                {8, 9}, {8, 14},
                {9, 10}, {9, 11}, {9, 12}, {9, 13},

                {11, 10}, {11, 11}, {11, 12}, {11, 13},
                {12, 9}, {12, 14},
                {13, 9}, {13, 14},
                {14, 10}, {14, 11}, {14, 12}, {14, 13},
        };


        for (int[] coord : pentaDecathlonCoords) {
            int row = coord[0];
            int col = coord[1];
            CELLS[row][col] = true;
        }

        drawGame();
    }

    public void cloverleaf() {
        clearCells();

        int[][] cloverleafCoords = {
                {6, 10}, {6, 11}, {6, 14}, {6, 15},
                {7, 9}, {7, 12}, {7, 13}, {7, 16},
                {8, 9}, {8, 12}, {8, 13}, {8, 16},
                {9, 10}, {9, 11}, {9, 14}, {9, 15},

                {11, 10}, {11, 11}, {11, 14}, {11, 15},
                {12, 9}, {12, 12}, {12, 13}, {12, 16},
                {13, 9}, {13, 12}, {13, 13}, {13, 16},
                {14, 10}, {14, 11}, {14, 14}, {14, 15},
        };

        for (int[] coord : cloverleafCoords) {
            int row = coord[0];
            int col = coord[1];
            CELLS[row][col] = true;
        }

        drawGame();
    }
}
