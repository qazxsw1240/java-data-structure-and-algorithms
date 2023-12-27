package src.algo.example.maze;

import src.algo.ArrayList;
import src.algo.ArrayStack;
import src.algo.List;
import src.algo.Stack;

public class MazeSolver {
    private final Maze maze;
    private final int width;
    private final int height;

    public MazeSolver(Maze maze) {
        this.maze = maze;
        this.width = this.maze.getWidth();
        this.height = this.maze.getHeight();
    }

    public static void main(String[] args) {
        MazeGenerator generator = new MazeGenerator(5, 5);
        Maze maze = generator.createMaze(0L, Coordinate.UNIT);
        MazeSolver application = new MazeSolver(maze);
        List<Maze> steps = application.solve(new Coordinate(8, 8));
        for (int i = 0; i < steps.size(); i += 1) {
            System.out.println(steps.get(i));
        }
    }

    public List<Maze> solve(Coordinate destination) {
        if (!isValidDestination(destination)) {
            throw new IllegalArgumentException();
        }
        Stack<Step> stack = new ArrayStack<>();
        boolean[][] visitMap = new boolean[this.height][this.width];
        Step lastStep = new Step(0, this.maze, null);
        for (stack.push(lastStep); !stack.isEmpty(); ) {
            lastStep = stack.peek();
            if (lastStep.maze.getCoordinate().equals(destination)) {
                while (!stack.isEmpty()) {
                    stack.pop();
                }
                break;
            }
            Coordinate coordinate = lastStep.maze.getCoordinate();
            visitMap[coordinate.y][coordinate.x] = true;
            List<Maze> neighbors = lastStep.maze.getNeighbors();
            List<Step> nextSteps = new ArrayList<>(neighbors.size());
            for (int i = 0; i < neighbors.size(); i += 1) {
                Maze neighbor = neighbors.get(i);
                Coordinate neighborCoordinate = neighbor.getCoordinate();
                // filters the visited points
                if (visitMap[neighborCoordinate.y][neighborCoordinate.x]) {
                    continue;
                }
                nextSteps.addLast(new Step(lastStep.level + 1, neighbor, lastStep));
            }
            if (nextSteps.isEmpty()) {
                stack.pop();
                continue;
            }
            for (int i = 0; i < nextSteps.size(); i += 1) {
                stack.push(nextSteps.get(i));
            }
        }
        Stack<Step> stepStack = new ArrayStack<>();
        while (lastStep != null) {
            stepStack.push(lastStep);
            lastStep = lastStep.previous;
        }
        List<Maze> steps = new ArrayList<>(stepStack.size());
        while (!stepStack.isEmpty()) {
            steps.addLast(stepStack.pop().maze);
        }
        return steps;
    }

    private boolean isValidDestination(Coordinate destination) {
        int x = destination.x;
        int y = destination.y;
        return (0 <= x && x < this.width) && (0 <= y && y < this.height);
    }

    private static class Step {
        final int level;
        final Maze maze;
        final Step previous;

        Step(int level, Maze maze, Step previous) {
            this.level = level;
            this.maze = maze;
            this.previous = previous;
        }
    }
}
