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

    private static final int BUFFER_X_OFFSET = 479, BUFFER_Y_OFFSET = 56; // TODO: Remove these offsets.
    private static final List<Point> changedCells = new ArrayList<Point>();
    public static int CELL_SIZE = 15, ROWS, COLUMNS;
    public static boolean[][] cells; // 2-dimensional array of boolean values (the cells)
    private static boolean drawGrid = true;
    private static Color cellColor = Color.black;
    private static Color FOREGROUND_COL = Color.white;
    private static Image imageBuffer;
    private static Graphics graphics;
    private static int canvasWidth, canvasHeight;
    public final int MIN_GRID_SIZE = 1;
    public int ANIMATION_SPEED = 100;
    public boolean pause = false;
    private boolean isDragging = false;

    public static void main(String[] args) {
        TheGame theGame = new TheGame();
        theGame.setupGUI();
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

        for (Point changedCell : changedCells) { // TODO: Fix concurrent mod exception, I suspect runGame() and threading is the cause.
            int row = changedCell.x;
            int col = changedCell.y;
            if (cells[row][col]) {
                graphics.setColor(cellColor);
                graphics.fillRect(row * CELL_SIZE, col * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            } else {
                graphics.setColor(FOREGROUND_COL);
                graphics.drawRect(row * CELL_SIZE, col * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        if (drawGrid && CELL_SIZE > 10) {
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

    public static void clearCells() {
        changedCells.clear();
        for (boolean[] cell : cells) {
            Arrays.fill(cell, false);
        }
        drawGame();
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
        UI.getFrame().setLocation(x / 2, y / 2);
        UI.getFrame().setTitle("The Game of Life");
        UI.getFrame().setVisible(true);
        UI.setDivider(0.2);

        // Run and pause
        UI.addButton("Run", () -> {
            pause = false;
            UI.clearText();
            runGame();
        });
        UI.addButton("Pause", () -> {
            pause = true;
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
            Seeds.rand();
        });
        UI.addButton("Glider", () -> {
            Seeds.glider();
        });
        UI.addButton("Gospel Glider Gun", () -> {
            Seeds.GGG();
        });
        UI.addButton("Lightweight Spaceship", () -> {
            Seeds.LWSS();
        });
        UI.addButton("Heavyweight Spaceship", () -> {
            Seeds.HWSS();
        });
        UI.addButton("Penta-Decathlon", () -> {
            Seeds.pentaDecathlon();
        });
        UI.addButton("Pulsar", () -> {
            Seeds.pulsar();
        });
        UI.addButton("Cloverleaf", () -> {
            Seeds.cloverleaf();
        });

        // Speed Adjustment
        UI.addSlider("Generation Speed", 0, 500, ANIMATION_SPEED, (double v) -> {
            ANIMATION_SPEED = (int) v;
            drawGame();
        });

        // Utilities
        UI.addButton("Cell Color", () -> {
            Color chosenColor = JColorChooser.showDialog(null, "Choose Highlight Color", cellColor);
            if (chosenColor != null) {  // If user didn't cancel
                cellColor = chosenColor; // Change color
            }
        });
        UI.addButton("Grid Visibility", () -> {
            drawGrid = !drawGrid;
            drawGame();
        });
        UI.addButton("Dark Mode", () -> {
            FOREGROUND_COL = Color.black;
            cellColor = Color.white;
            drawGame();
        });
        UI.addButton("Quit", UI::quit);

        initializeGrid(); // Must be before mouse init
        UI.setMouseMotionListener(this::doMouse);
        drawGame();
        print("      Welcome to The Game of Life       ");
        print("========================================");
        print("  Select cells and click run to start!  ");
    }

    private void doMouse(String action, double x, double y) {
        int gridX = (int) (x / CELL_SIZE);
        int gridY = (int) (y / CELL_SIZE);
        if (gridX >= 0 && gridX < cells.length && gridY >= 0 && gridY < cells[0].length) {
            if (action.equals("pressed")) {
                isDragging = true;  // set dragging state to true on mouse press
                cells[gridX][gridY] = !cells[gridX][gridY];
                UI.printMessage(" Selected Coordinate: row " + gridX + ", column " + gridY);
                drawGame();
            } else if (action.equals("dragged") && isDragging) {
                cells[gridX][gridY] = true;
                changedCells.add(new Point(gridX, gridY));
                UI.printMessage(" Selected Coordinate: row " + gridX + ", column " + gridY);
                drawGame();
            } else if (action.equals("released")) {
                isDragging = false;  // reset dragging state on mouse release
            }
        }
    }

    private void runGame() {
        TheLife.doStep(cells);
        while (TheLife.hasActiveCells() && !pause) {
            drawGame();
            TheLife.doStep(cells);
            try {
                Thread.sleep(ANIMATION_SPEED);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void initializeGrid() {
        int canvasWidth = UI.getCanvasWidth();
        int canvasHeight = UI.getCanvasHeight();
        ROWS = canvasHeight / MIN_GRID_SIZE;
        COLUMNS = canvasWidth / MIN_GRID_SIZE;
        cells = new boolean[COLUMNS][ROWS];
        drawGame();
    }

    private void print(String s) {
        for (char c : s.toCharArray()) {
            UI.print(c);
            UI.sleep(15);
        }
        UI.println();
    }
}
