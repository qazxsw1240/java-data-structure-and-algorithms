package src.algo.example.maze;

import src.algo.ArrayList;
import src.algo.List;

public class Maze {
    private static final Coordinate[] OFFSETS = new Coordinate[]{
        new Coordinate(-1, 0),
        new Coordinate(1, 0),
        new Coordinate(0, -1),
        new Coordinate(0, 1)
    };

    private final MazeData data;
    private final Coordinate coordinate;

    public Maze(MazeData data, Coordinate coordinate) {
        this.data = data;
        this.coordinate = coordinate;
    }

    public int getWidth() {
        return this.data.getWidth();
    }

    public int getHeight() {
        return this.data.getHeight();
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public List<Maze> getNeighbors() {
        List<Maze> neighbors = new ArrayList<>(OFFSETS.length);
        for (int i = 0; i < OFFSETS.length; i += 1) {
            Coordinate offset = OFFSETS[i];
            Coordinate newCoordinate = this.coordinate.add(offset);
            if (this.data.getCell(newCoordinate) == MazeCell.ROAD) {
                neighbors.addLast(new Maze(this.data, newCoordinate));
            }
        }
        return neighbors;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(MazeCell.WALL.toString().repeat(this.data.getWidth() + 2));
        builder.append("\n");
        MazeCell[][] tiles = this.data.asArray();
        for (int i = 0; i < tiles.length; i += 1) {
            MazeCell[] line = tiles[i];
            builder.append(MazeCell.WALL);
            for (int j = 0; j < line.length; j += 1) {
                if (i == this.coordinate.y && j == this.coordinate.x) {
                    builder.append("O");
                    continue;
                }
                builder.append(line[j]);
            }
            builder.append(MazeCell.WALL);
            builder.append("\n");
        }
        builder.append(MazeCell.WALL.toString().repeat(this.data.getWidth() + 2));
        return builder.toString();
    }
}
