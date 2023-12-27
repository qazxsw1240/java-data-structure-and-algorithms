package src.algo.example.maze;

import src.algo.ArrayList;
import src.algo.ArrayStack;
import src.algo.List;
import src.algo.Stack;

import java.util.Random;

public class MazeGenerator {
    private static final Coordinate[] OFFSETS = new Coordinate[]{
        new Coordinate(-2, 0),
        new Coordinate(2, 0),
        new Coordinate(0, -2),
        new Coordinate(0, 2)
    };

    private final int width;
    private final int height;

    public MazeGenerator(int widthDimension, int heightDimension) {
        this.width = widthDimension * 2 - 1;
        this.height = heightDimension * 2 - 1;
    }

    private static void connectCells(MazeData data, Coordinate start, Coordinate end) {
        Coordinate middle = start.add(end).divide(2);
        data.setCell(start, MazeCell.ROAD);
        data.setCell(middle, MazeCell.ROAD);
        data.setCell(start, MazeCell.ROAD);
    }

    public Maze createMaze(Coordinate coordinate) {
        return createMaze(System.currentTimeMillis(), coordinate);
    }

    public Maze createMaze(long seed, Coordinate coordinate) {
        MazeData data = new MazeData(this.width, this.height);
        Coordinate newCoordinate = coordinate.multiply(2);
        Random random = new Random(seed);
        fillTiles(data, newCoordinate, random);
        return new Maze(data, newCoordinate);
    }

    private void fillTiles(MazeData data, Coordinate coordinate, Random random) {
        Stack<Coordinate> stack = new ArrayStack<>();
        stack.push(coordinate);
        while (!stack.isEmpty()) {
            Coordinate top = stack.peek();
            if (data.getCell(top) != MazeCell.ROAD) {
                data.setCell(top, MazeCell.ROAD);
            }
            List<Coordinate> neighbors = getNeighbors(data, top);
            if (neighbors.isEmpty()) {
                stack.pop();
                continue;
            }
            Coordinate next = neighbors.get(random.nextInt(neighbors.size()));
            connectCells(data, top, next);
            stack.push(next);
        }
    }

    private List<Coordinate> getNeighbors(MazeData data, Coordinate coordinate) {
        int length = OFFSETS.length;
        List<Coordinate> neighbors = new ArrayList<>(length);
        for (int i = 0; i < length; i += 1) {
            Coordinate offset = OFFSETS[i];
            Coordinate newCoordinate = coordinate.add(offset);
            // filters the visited neighbors
            if (data.getCell(newCoordinate) == MazeCell.WALL) {
                neighbors.addLast(newCoordinate);
            }
        }
        return neighbors;
    }
}
