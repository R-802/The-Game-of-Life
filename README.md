# The Game of Life

This repository contains a Java implementation of Conway's Game of Life, a cellular automaton devised by the British mathematician John Horton Conway. The game is a zero-player game, meaning that its evolution is determined by its initial state, requiring no further input from humans.

## Getting Started

To get started, clone the repository to your local machine.

```bash
git clone https://github.com/R-802/The-Game-of-Life.git
```

Navigate to the `main` directory within the repository:

```bash
cd The-Game-of-Life/main
```

Here you'll find the primary Java class `TheGame.java` which contains the main method to run the program.

## How to Run

Ensure you have a Java environment set up on your machine, then compile and run `TheGame.java`.

## Features

- **Canvas**: A white canvas where the cells live. Cells can be toggled on and off by clicking on them.
- **Run & Pause Buttons**: Start and pause the game simulation.
- **Cell Color Button**: Change the color of the active cells.
- **Grid Size Slider**: Adjust the size of the grid.
- **Simulation Speed Slider**: Adjust the speed of the simulation.
- **Pre-defined Seeds**: Buttons to create known starting configurations like Gliders, Lightweight Spaceship, etc.
- **Grid Visibility Button**: Toggle the visibility of the grid.
- **Clear Cells Button**: Clears all cells on the board.
- **Quit Button**: Exit the program.

## Game Controls

- **Mouse Clicks**: Toggle the state of cells by clicking on the canvas.
- **Run Button**: Starts the simulation.
- **Pause Button**: Pauses the simulation.

## Code Structure

The `TheGame` class is the entry point of the program and handles the GUI setup and user interactions. It relies on other classes such as `TheLife` for simulation steps and `Seeds` for predefined cell configurations.

The `drawGame` method is responsible for rendering the cells and grid on the canvas. The `runGame` method is the core loop of the simulation, continuously updating the state of the cells and redrawing the canvas.

## Dependencies

- The program relies on the `ecs100` library for the UI components.
- Utility classes `TheLife` and `Seeds` for simulation logic and predefined cell configurations respectively.
