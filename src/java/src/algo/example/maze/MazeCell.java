package src.algo.example.maze;

public enum MazeCell {
    ROAD(" "),
    WALL("#");

    private final String stringValue;

    private MazeCell(String code) {
        this.stringValue = code;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }
}
