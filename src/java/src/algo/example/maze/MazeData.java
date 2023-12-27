package src.algo.example.maze;

import java.util.Arrays;

public class MazeData {
    private final int width;
    private final int height;
    private final MazeCell[][] cells;

    public MazeData(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new MazeCell[this.height][this.width];
        initializeCells();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public MazeCell getCell(Coordinate coordinate) {
        int x = coordinate.x;
        int y = coordinate.y;
        if ((x < 0 || x >= this.width) || (y < 0 || y >= this.height)) {
            return null;
        }
        return this.cells[y][x];
    }

    public void setCell(Coordinate coordinate, MazeCell value) {
        int x = coordinate.x;
        int y = coordinate.y;
        if ((x < 0 || x >= this.width) || (y < 0 || y >= this.height)) {
            return;
        }
        this.cells[y][x] = value;
    }

    public MazeCell[][] asArray() {
        return this.cells;
    }

    private void initializeCells() {
        for (MazeCell[] line : this.cells) {
            Arrays.fill(line, MazeCell.WALL);
        }
    }
}
