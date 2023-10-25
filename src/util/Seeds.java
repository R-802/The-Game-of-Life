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
}
