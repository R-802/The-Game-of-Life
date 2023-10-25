package util;

import ecs100.UI;
import main.TheGame;

import static main.TheGame.drawGame;

public class TheLife {
    private static int activeCellCount = 0;

    public static void doStep(boolean[][] grid) {
        boolean[][] newGrid = new boolean[grid.length][grid[0].length];
        int newActiveCellCount = 0;  // Reset the active cell count
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                int neighbors = countNeighbors(grid, row, col);

                // If cell is active and has less than 2 or more than 3 neighbors
                if (grid[row][col] && (neighbors < 2 || neighbors > 3)) {
                    newGrid[row][col] = false;  // Cell dies

                    // If cell is inactive and has 3 neighbors;
                } else if (!grid[row][col] && neighbors == 3) {
                    newGrid[row][col] = true;  // Cell is born
                    newActiveCellCount++;  // Increment the active cell count

                    // Else do nothing
                } else {
                    newGrid[row][col] = grid[row][col];  // Cell stays the same
                    if (grid[row][col]) {
                        newActiveCellCount++;  // Increment the active cell count
                    }
                }
            }
        }
        activeCellCount = newActiveCellCount;  // Update the active cell count
        UI.clearText();
        UI.println("There are " + activeCellCount + " active cells");
        TheGame.CELLS = newGrid;
        drawGame();
    }

    private static int countNeighbors(boolean[][] grid, int row, int col) {
        int neighbors = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (!(dr == 0 && dc == 0) && // Skip the cell itself
                        ((row + dr) >= 0) && ((row + dr) < grid.length) && ((col + dc) >= 0) && ((col + dc) < grid[row].length) && grid[row + dr][col + dc]) {
                    neighbors++;
                }
            }
        }
        return neighbors;
    }

    public static boolean hasActiveCells() {
        return activeCellCount > 0;
    }
}
