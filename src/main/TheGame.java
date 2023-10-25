package main;

import ecs100.UI;
import operations.TheLife;

import java.awt.*;

import static operations.TheLife.doStep;

public class TheGame {

    public static final int MIN_GRID_SIZE = 5;
    private static final int BUFFER_X_OFFSET = 460, BUFFER_Y_OFFSET = 56; // TODO: Remove these offsets.
    public static int CELL_SIZE = 15, ROWS, COLUMNS;
    public static boolean[][] CELLS; // 2-dimensional array of boolean values (the cells)
    public static boolean PAUSE = false;
    private static boolean DRAW_GRID = true;

    public static void drawGame() {
        int canvasWidth = UI.getCanvasWidth();
        int canvasHeight = UI.getCanvasHeight();
        Image imageBuffer = UI.getFrame().createVolatileImage(canvasWidth, canvasHeight);
        Graphics graphics = imageBuffer.getGraphics();

        // Adaptive buffer adjustment
        if (UI.getCanvasWidth() != canvasWidth || UI.getCanvasHeight() != canvasHeight) {
            canvasWidth = UI.getCanvasWidth();
            canvasHeight = UI.getCanvasHeight();
            imageBuffer = UI.getFrame().createVolatileImage(canvasWidth, canvasHeight);
            graphics = imageBuffer.getGraphics();
        }

        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, canvasWidth, canvasHeight);

        // For each cell in the grid, check if active
        for (int row = 0; row < CELLS.length; row++) {
            for (int col = 0; col < CELLS[row].length; col++) {
                if (CELLS[row][col]) { // Draw if the cell is active
                    graphics.setColor(Color.black);
                    graphics.fillRect(row * CELL_SIZE, col * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else { // Erase if the cell is inactive
                    graphics.setColor(Color.white);
                    graphics.drawRect(row * CELL_SIZE, col * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        if (DRAW_GRID) {
            // Draw the gridlines
            graphics.setColor(Color.black);
            for (int x = 0; x <= canvasWidth; x += CELL_SIZE) {
                graphics.drawLine(x, 0, x, canvasHeight);
            }
            for (int y = 0; y <= canvasHeight; y += CELL_SIZE) {
                graphics.drawLine(0, y, canvasWidth, y);
            }
        }

        Graphics overlay = UI.getFrame().getGraphics();
        overlay.drawImage(imageBuffer, BUFFER_X_OFFSET, BUFFER_Y_OFFSET, null);
        graphics.dispose();
        overlay.dispose();
    }

    private static void initializeGrid() {
        int canvasWidth = UI.getCanvasWidth();
        int canvasHeight = UI.getCanvasHeight();
        int rows = canvasHeight / MIN_GRID_SIZE;
        int columns = canvasWidth / MIN_GRID_SIZE;
        CELLS = new boolean[rows][columns];
        drawGame();
    }

    public static void main(String[] args) {
        TheGame theGame = new TheGame();
        theGame.setupGUI();
    }

    public void setupGUI() {
        UI.initialise(); // Initialize UI and configure window properties
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Set the window to be three quarters of the screen's width and height
        int width = (int) (screenSize.getWidth() * 0.75);
        int height = (int) (screenSize.getHeight() * 0.75);
        UI.setWindowSize(width, height);

        // Center the window on the users screen
        int x = (int) ((screenSize.getWidth() - width) / 2);
        int y = (int) ((screenSize.getHeight() - height) / 2);
        UI.getFrame().setLocation(x, y);
        UI.getFrame().setTitle("The Game of Life");
        UI.getFrame().setVisible(true);
        UI.setDivider(0.2);

        // Mouse setup
        UI.setMouseMotionListener(this::doMouse);
        UI.setMouseListener(this::doMouse);

        // Dynamic grid and cell size adjustment
        UI.addSlider("Grid Size", MIN_GRID_SIZE, 25, CELL_SIZE, (double v) -> {
            CELL_SIZE = (int) v;
            UI.clearText();
            UI.clearGraphics();
            drawGame();
        });
        UI.addButton("Run", () -> {
            PAUSE = false;
            UI.clearText();
            runGame();
        });
        UI.addButton("Pause", () -> {
            PAUSE = true;
            drawGame();
        });
        UI.addButton("Toggle Grid Visibility", () -> {
            DRAW_GRID = !DRAW_GRID;
            drawGame();
        });
        UI.addButton("Clear Cells", this::clearCells);
        UI.addButton("Quit", UI::quit);
        ROWS = UI.getCanvasWidth();
        COLUMNS = UI.getCanvasHeight();
        initializeGrid();
        drawGame();

        // Print sequence
        print("      Welcome to The Game of Life       ");
        print("========================================");
        print("  Select cells and click run to start!  ");
    }

    public void print(String s) {
        for (char c : s.toCharArray()) {
            UI.print(c);
            UI.sleep(15);
        }
        UI.println();
    }

    public void doMouse(String action, double x, double y) {
        if (action.equals("pressed")) {
            int gridX = (int) (x / CELL_SIZE);
            int gridY = (int) (y / CELL_SIZE);
            if (gridX >= 0 && gridX < CELLS.length && gridY >= 0 && gridY < CELLS[0].length) {
                CELLS[gridX][gridY] = !CELLS[gridX][gridY];
                drawGame();
            }
        }
    }

    public void runGame() {
        while (TheLife.hasActiveCells() && !PAUSE) {
            drawGame();
            doStep(CELLS);
            try {
                Thread.sleep(100);  // Pause for a short duration before the next iteration, to make changes visible
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Restore interrupted status
                break;  // Exit the loop if the thread is interrupted
            }
        }
    }

    private void clearCells() {
        for (int row = 0; row < CELLS.length; row++) {
            for (int col = 0; col < CELLS[row].length; col++) {
                CELLS[row][col] = false;
            }
        }
        drawGame();
    }
}
