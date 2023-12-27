package src.algo.example.maze;

public class Coordinate {
    public static final Coordinate UNIT = new Coordinate(0, 0);

    public final int x;
    public final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate add(Coordinate coordinate) {
        return new Coordinate(this.x + coordinate.x, this.y + coordinate.y);
    }

    public Coordinate multiply(int scalar) {
        return new Coordinate(this.x * scalar, this.y * scalar);
    }

    public Coordinate divide(int scalar) {
        return new Coordinate(this.x / scalar, this.y / scalar);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Coordinate)) {
            return false;
        }
        Coordinate coord = (Coordinate) obj;
        return coord.x == this.x && coord.y == this.y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }
}
