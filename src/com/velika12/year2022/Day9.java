package com.velika12.year2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day9 {

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File("input/year2022/input_day9.txt"))) {

            Rope rope = new Rope(10);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] input = line.split(" ");

                Direction direction = Direction.getByCode(input[0]);
                int steps = Integer.parseInt(input[1]);

                rope.moveHead(direction, steps);
            }

            System.out.println("Number of positions tail visited: " + rope.getNumberOfPositionsTailVisited());
        }
    }
}

class Rope {
    private final LinkedList<Knot> knots = new LinkedList<>();
    private final Knot head;
    private final Knot tail;

    private final Set<Coordinate> tailPath = new HashSet<>();

    public Rope(int length) {
        while (length > 0) {
            knots.add(new Knot());
            length--;
        }

        head = knots.getFirst();
        tail = knots.getLast();

        tailPath.add(tail.getPosition());
    }

    public void moveHead(Direction direction, int steps) {
        for (int i = 0; i < steps; i++) {
            head.move(direction);

            Knot previous = head;
            Iterator<Knot> it = knots.listIterator(1);
            while (it.hasNext()) {
                Knot current = it.next();

                if (current.isTouching(previous)) {
                    break;
                }

                current.moveTowards(previous);

                previous = current;
            }

            tailPath.add(tail.getPosition());
        }
    }

    public int getNumberOfPositionsTailVisited() {
        return tailPath.size();
    }
}

class Coordinate {
    private int x = 0;
    private int y = 0;

    public Coordinate() {}

    private Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void increaseX() {
        x++;
    }

    public void decreaseX() {
        x--;
    }

    public void increaseY() {
        y++;
    }

    public void decreaseY() {
        y--;
    }

    public int getDiffXUpon(Coordinate coordinate) {
        return coordinate.x - x;
    }

    public int getDiffYUpon(Coordinate coordinate) {
        return coordinate.y - y;
    }

    public Coordinate copy() {
        return new Coordinate(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

class Knot {

    private final Coordinate position = new Coordinate();

    public void move(Direction direction) {
        switch (direction) {
            case UP:
                position.increaseY();
                break;
            case LEFT:
                position.decreaseX();
                break;
            case RIGHT:
                position.increaseX();
                break;
            case DOWN:
                position.decreaseY();
                break;
            case RIGHT_UP:
                position.increaseX();
                position.increaseY();
                break;
            case LEFT_UP:
                position.decreaseX();
                position.increaseY();
                break;
            case RIGHT_DOWN:
                position.increaseX();
                position.decreaseY();
                break;
            case LEFT_DOWN:
                position.decreaseX();
                position.decreaseY();
                break;
            default:
        }
    }

    public boolean isTouching(Knot knot) {
        int dx = position.getDiffXUpon(knot.position);
        int dy = position.getDiffYUpon(knot.position);
        return (dx*dx + dy*dy) <= 2;
    }

    public void moveTowards(Knot knot) {
        int dx = position.getDiffXUpon(knot.position);
        int dy = position.getDiffYUpon(knot.position);

        if (dy == 0) {
            // in the same row
            if (dx > 0) {
                move(Direction.RIGHT);
            } else {
                move(Direction.LEFT);
            }
        } else if (dx == 0) {
            // in the same column
            if (dy > 0) {
                move(Direction.UP);
            } else {
                move(Direction.DOWN);
            }
        } else {
            // in different rows and columns
            if (dx > 0) {
                if (dy > 0) {
                    move(Direction.RIGHT_UP);
                } else {
                    move(Direction.RIGHT_DOWN);
                }
            } else {
                if (dy > 0) {
                    move(Direction.LEFT_UP);
                } else {
                    move(Direction.LEFT_DOWN);
                }
            }
        }
    }

    public Coordinate getPosition() {
        return position.copy();
    }
}

enum Direction {
    RIGHT("R"), UP("U"), LEFT("L"), DOWN("D"),
    RIGHT_UP(null), LEFT_UP(null), RIGHT_DOWN(null), LEFT_DOWN(null);

    Direction(String code) {
        this.code = code;
    }

    private final String code;

    public static Direction getByCode(String code) {
        for (Direction direction : values()) {
            if (direction.code.equals(code)) {
                return direction;
            }
        }

        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
