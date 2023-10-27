package util;

import ecs100.UI;
import main.TheGame;

import static main.TheGame.drawGame;

public class TheLife {
    private static int activeCellCount = 0;

    private static void doStep(boolean[][] grid) {
        boolean[][] newGrid = new boolean[grid.length][grid[0].length];
        int newActiveCellCount = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                int neighbors = countNeighbors(grid, row, col);
                newGrid[row][col] = (grid[row][col] && (neighbors == 2 || neighbors == 3)) || (!grid[row][col] && neighbors == 3);
                if (newGrid[row][col]) {
                    newActiveCellCount++;
                }
            }
        }
        activeCellCount = newActiveCellCount;  // Update the active cell count
    }

    private static int countNeighbors(boolean[][] grid, int row, int col) {
        int neighbors = 0;
        outerLoop:
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (!(dr == 0 && dc == 0) && ((row + dr) >= 0) && ((row + dr) < grid.length) && ((col + dc) >= 0) && ((col + dc) < grid[row].length) && grid[row + dr][col + dc]) {
                    neighbors++;
                    if (neighbors == 4) {
                        break outerLoop; // No state change can occur with 4 neighbors
                    }
                }
            }
        }
        return neighbors;
    }

    public static boolean hasActiveCells() {
        return activeCellCount > 0;
    }

    private static void updateUI() {
        UI.clearText();
        UI.println("There are " + activeCellCount + " active cells");
    }

    public static void handleStep(boolean[][] grid) {
        doStep(grid);
        updateUI();
        TheGame.cells = grid;
        drawGame();
    }
}
