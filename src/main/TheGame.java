package main;

import ecs100.UI;
import util.Seeds;
import util.TheLife;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TheGame {

    public static final int MIN_GRID_SIZE = 1;
    private static final int BUFFER_X_OFFSET = 479, BUFFER_Y_OFFSET = 56; // TODO: Remove these offsets.
    private static final List<Point> changedCells = new ArrayList<Point>();
    public static int ANIMATION_SPEED = 100;
    public static int CELL_SIZE = 15, ROWS, COLUMNS;
    public static boolean[][] CELLS; // 2-dimensional array of boolean values (the cells)
    public static boolean PAUSE = false;
    private static boolean DRAW_GRID = true;
    private static Color CELL_COLOR = Color.black;

    /*----------------Graphics----------------*/
    private static Color FOREGROUND_COL = Color.white;
    // Fields to hold persistent objects
    private static Image imageBuffer;
    private static Graphics graphics;
    private static int canvasWidth, canvasHeight;
    private static boolean isDragging = false;  // new field to keep track of dragging state

    public static void setupGUI() {
        UI.initialise(); // Initialize UI and configure window properties
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Set the window to be three quarters of the screen's width and height
        int width = (int) (screenSize.getWidth() * 0.75);
        int height = (int) (screenSize.getHeight() * 0.75);
        UI.setWindowSize(width, height);

        // Center the window on the users screen
        int x = (int) ((screenSize.getWidth() - width) / 2);
        int y = (int) ((screenSize.getHeight() - height) / 2);
        UI.getFrame().setLocation(x / 2, y / 2);
        UI.getFrame().setTitle("The Game of Life");
        UI.getFrame().setVisible(true);
        UI.setDivider(0.2);

        // Run and pause
        UI.addButton("Run", () -> {
            PAUSE = false;
            UI.clearText();
            runGame();
        });
        UI.addButton("Pause", () -> {
            PAUSE = true;
            UI.clearText();
            print("Paused");
            drawGame();
        });
        UI.addButton("Clear Cells", () -> {
            clearCells();
            UI.clearText();
        });

        // Dynamic grid and cell size adjustment
        UI.addSlider("Cell Size", MIN_GRID_SIZE, 100, CELL_SIZE, (double v) -> {
            CELL_SIZE = (int) v;
            UI.clearGraphics();
            UI.printMessage(" Grid Size: " + CELL_SIZE);
            drawGame();
        });

        // Seeds
        UI.addButton("Random Fill", () -> {
            Seeds seeds = new Seeds();
            seeds.rand();
        });
        UI.addButton("Glider", () -> {
            Seeds seeds = new Seeds();
            seeds.glider();
        });
        UI.addButton("Gospel Glider Gun", () -> {
            Seeds seeds = new Seeds();
            seeds.GGG();
        });
        UI.addButton("Lightweight Spaceship", () -> {
            Seeds seeds = new Seeds();
            seeds.LWSS();
        });
        UI.addButton("Heavyweight Spaceship", () -> {
            Seeds seeds = new Seeds();
            seeds.HWSS();
        });
        UI.addButton("Penta-Decathlon", () -> {
            Seeds seeds = new Seeds();
            seeds.pentaDecathlon();
        });
        UI.addButton("Pulsar", () -> {
            Seeds seeds = new Seeds();
            seeds.pulsar();
        });
        UI.addButton("Cloverleaf", () -> {
            Seeds seeds = new Seeds();
            seeds.cloverleaf();
        });

        // Speed Adjustment
        UI.addSlider("Generation Speed", 0, 500, ANIMATION_SPEED, (double v) -> {
            ANIMATION_SPEED = (int) v;
            drawGame();
        });

        // Utilities
        UI.addButton("Cell Color", () -> {
            Color chosenColor = JColorChooser.showDialog(null, "Choose Highlight Color", CELL_COLOR);
            if (chosenColor != null) {  // If user didn't cancel
                CELL_COLOR = chosenColor; // Change color
            }
        });
        UI.addButton("Grid Visibility", () -> {
            DRAW_GRID = !DRAW_GRID;
            drawGame();
        });
        UI.addButton("Dark Mode", () -> {
            FOREGROUND_COL = Color.black;
            CELL_COLOR = Color.white;
            drawGame();
        });
        UI.addButton("Quit", UI::quit);
        initializeGrid(); // Must be before mouse init
        UI.setMouseMotionListener(TheGame::doMouse);
        drawGame();
        print("      Welcome to The Game of Life       ");
        print("========================================");
        print("  Select cells and click run to start!  ");
    }

    public static void drawGame() {
            // Initialize persistent objects if null or canvas size changed
            int newCanvasWidth = UI.getCanvasWidth(), newCanvasHeight = UI.getCanvasHeight();
            if (imageBuffer == null || graphics == null || newCanvasWidth != canvasWidth || newCanvasHeight != canvasHeight) {
                canvasWidth = newCanvasWidth;
                canvasHeight = newCanvasHeight;
                if (graphics != null) graphics.dispose();  // Dispose of old graphics object
                imageBuffer = UI.getFrame().createVolatileImage(canvasWidth, canvasHeight);
                graphics = imageBuffer.getGraphics();
            }

            graphics.setColor(FOREGROUND_COL);
            graphics.fillRect(0, 0, canvasWidth, canvasHeight);

            for (Point changedCell : changedCells) { // TODO: Fix concurrent mod exception
                int row = changedCell.x;
                int col = changedCell.y;
                if (CELLS[row][col]) {
                    graphics.setColor(CELL_COLOR);
                    graphics.fillRect(row * CELL_SIZE, col * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else {
                    graphics.setColor(FOREGROUND_COL);
                    graphics.drawRect(row * CELL_SIZE, col * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }

            if (DRAW_GRID && CELL_SIZE > 10) {
                graphics.setColor(Color.black);
                int max = (canvasWidth >= canvasHeight) ? canvasWidth : canvasHeight;
                for (int i = 0; i <= max; i += CELL_SIZE) {
                    graphics.drawLine(i, 0, i, canvasHeight);  // Vertical lines
                    graphics.drawLine(0, i, canvasWidth, i);  // Horizontal lines
                }
            }

            Graphics overlay = UI.getFrame().getGraphics();
            overlay.drawImage(imageBuffer, BUFFER_X_OFFSET, BUFFER_Y_OFFSET, null);
            overlay.dispose();
    }

    private static void doMouse(String action, double x, double y) {
        int gridX = (int) (x / CELL_SIZE);
        int gridY = (int) (y / CELL_SIZE);
        if (gridX >= 0 && gridX < CELLS.length && gridY >= 0 && gridY < CELLS[0].length) {
            if (action.equals("pressed")) {
                isDragging = true;  // set dragging state to true on mouse press
                CELLS[gridX][gridY] = !CELLS[gridX][gridY];
                UI.printMessage(" Selected Coordinate: row " + gridX + ", column " + gridY);
                drawGame();
            } else if (action.equals("dragged") && isDragging) {
                CELLS[gridX][gridY] = true;
                changedCells.add(new Point(gridX, gridY));
                UI.printMessage(" Selected Coordinate: row " + gridX + ", column " + gridY);
                drawGame();
            } else if (action.equals("released")) {
                isDragging = false;  // reset dragging state on mouse release
            }
        }
    }


    /*----------------Util----------------*/

    private static void runGame() {
        TheLife.doStep(CELLS);
        while (TheLife.hasActiveCells() && !PAUSE) {
            drawGame();
            TheLife.doStep(CELLS);
            try {
                Thread.sleep(ANIMATION_SPEED);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private static void initializeGrid() {
        int canvasWidth = UI.getCanvasWidth();
        int canvasHeight = UI.getCanvasHeight();
        ROWS = canvasHeight / MIN_GRID_SIZE;
        COLUMNS = canvasWidth / MIN_GRID_SIZE;
        CELLS = new boolean[COLUMNS][ROWS];
        drawGame();
    }

    public static void clearCells() {
        changedCells.clear();
        for (boolean[] cell : CELLS) {
            Arrays.fill(cell, false);
        }
        drawGame();
    }

    public static void print(String s) {
        for (char c : s.toCharArray()) {
            UI.print(c);
            UI.sleep(15);
        }
        UI.println();
    }

    public static void main(String[] args) {
        TheGame theGame = new TheGame();
        setupGUI();
    }
}
