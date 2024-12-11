package com.velika12.year2024;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

public class Day6 {

    private static final List<List<Point>> map = new ArrayList<>();
    private static Point start;
    private static final Set<Point> obstructions = new LinkedHashSet<>();

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input\\year2024\\input_day6.txt"))) {
            int i = 0;

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                final int y = i;
                List<Point> line = IntStream.range(0, str.length()).mapToObj(x -> new Point(x, y, str.charAt(x))).toList();
                map.add(line);

                if (start == null) {
                    int startX = findStartX(line);
                    if (startX != -1) {
                        start = line.get(startX);
                    }
                }

                i++;
            }

            Guard guard = new Guard(start, WalkDirection.of(start.c()), new HashMap<>());
            while (walk(guard, null) == WalkResult.IN_PROGRESS) ;

            System.out.println("Visited positions: " + guard.visited.size());

            //obstructions.forEach(System.out::println);

            System.out.println("Obstructions: " + obstructions.size());

            printMap(guard);
        }
    }

    private static int findStartX(List<Point> line) {
        int i = 0;
        while (i < line.size()) {
            if (!Set.of('.', '#').contains(line.get(i).c())) {
                return i;
            }

            i++;
        }

        return -1;
    }

    private static void printMap(Guard guard) {
        System.out.println("---");
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(0).size(); x++) {
                Point point = map.get(y).get(x);
                if (guard.visited.containsKey(point)) {
                    if (start.equals(point)) {
                        System.out.print(green(point.c()));
                    } else if (obstructions.contains(point)) {
                        System.out.print(red('o'));
                    } else if (guard.visited.get(point).contains(WalkDirection.UP) && guard.visited.get(point).contains(WalkDirection.RIGHT)) {
                        System.out.print("+");
                    } else if (guard.visited.get(point).contains(WalkDirection.RIGHT) && guard.visited.get(point).contains(WalkDirection.DOWN)) {
                        System.out.print("+");
                    } else if (guard.visited.get(point).contains(WalkDirection.DOWN) && guard.visited.get(point).contains(WalkDirection.LEFT)) {
                        System.out.print("+");
                    } else if (guard.visited.get(point).contains(WalkDirection.LEFT) && guard.visited.get(point).contains(WalkDirection.UP)) {
                        System.out.print("+");
                    } else if (guard.visited.get(point).contains(WalkDirection.RIGHT) || guard.visited.get(point).contains(WalkDirection.LEFT)) {
                        System.out.print("-");
                    } else {
                        System.out.print("|");
                    }
                } else {
                    System.out.print(point.c());
                }
            }
            System.out.println();
        }
        System.out.println("---");
    }

    private static String red(char c) {
        return "\u001B[31m" + c + "\u001B[0m";
    }

    private static String green(char c) {
        return "\u001B[32m" + c + "\u001B[0m";
    }

    private static WalkResult walk(Guard guard, Point obstruction) {
        if (guard.visited()) {
            return WalkResult.LOOP_DETECTED;
        }

        guard.visit();

        int x = guard.point.x();
        int y = guard.point.y();
        WalkDirection direction = guard.direction;
        Point nextPoint;

        switch (direction) {
            case UP:
                if (y == 0) {
                    return WalkResult.END;
                }

                nextPoint = map.get(y - 1).get(x);
                if (nextPoint.c() == '#' || nextPoint.equals(obstruction)) {
                    direction = WalkDirection.RIGHT;
                } else {
                    if (obstruction == null) {
                        checkObstruction(guard, nextPoint, WalkDirection.RIGHT);
                    }
                    y--;
                }
                break;
            case RIGHT:
                if (x == map.get(0).size() - 1) {
                    return WalkResult.END;
                }

                nextPoint = map.get(y).get(x + 1);
                if (nextPoint.c() == '#' || nextPoint.equals(obstruction)) {
                    direction = WalkDirection.DOWN;
                } else {
                    if (obstruction == null) {
                        checkObstruction(guard, nextPoint, WalkDirection.DOWN);
                    }
                    x++;
                }
                break;
            case DOWN:
                if (y == map.size() - 1) {
                    return WalkResult.END;
                }

                nextPoint = map.get(y + 1).get(x);
                if (nextPoint.c() == '#' || nextPoint.equals(obstruction)) {
                    direction = WalkDirection.LEFT;
                } else {
                    if (obstruction == null) {
                        checkObstruction(guard, nextPoint, WalkDirection.LEFT);
                    }
                    y++;
                }
                break;
            case LEFT:
                if (x == 0) {
                    return WalkResult.END;
                }

                nextPoint = map.get(y).get(x - 1);
                if (nextPoint.c() == '#' || nextPoint.equals(obstruction)) {
                    direction = WalkDirection.UP;
                } else {
                    if (obstruction == null) {
                        checkObstruction(guard, nextPoint, WalkDirection.UP);
                    }
                    x--;
                }
                break;
        }

        guard.move(map.get(y).get(x), direction);

        return WalkResult.IN_PROGRESS;
    }

    private static void checkObstruction(Guard guard, Point oPoint, WalkDirection turnDirection) {
        if (oPoint.c() != '.' || guard.visited(oPoint)) {
            return;
        }

        Guard phantomGuard = new Guard(guard.point, turnDirection, guard.visited);
        WalkResult result;
        do {
            result = walk(phantomGuard, oPoint);
            if (result == WalkResult.LOOP_DETECTED) {
                obstructions.add(oPoint);
                return;
            }
        } while (result != WalkResult.END);
    }
}

record Point(int x, int y, char c) {}

class Guard {
    Point point;
    WalkDirection direction;
    Map<Point, Set<WalkDirection>> visited = new LinkedHashMap<>();

    Guard(Point point, WalkDirection direction, Map<Point, Set<WalkDirection>> visited) {
        move(point, direction);
        visited.forEach((p, dirs) -> dirs.forEach(dir -> visit(p, dir)));
    }

    void move(Point point, WalkDirection direction) {
        this.point = point;
        this.direction = direction;
    }

    void visit() {
        visit(point, direction);
    }

    private void visit(Point point, WalkDirection direction) {
        if (!visited.containsKey(point)) {
            visited.put(point, new HashSet<>());
        }
        visited.get(point).add(direction);
    }

    boolean visited(Point point) {
        return visited.containsKey(point);
    }

    boolean visited() {
        return visited.containsKey(point) && visited.get(point).contains(direction);
    }
}

enum WalkDirection {
    UP('^'), RIGHT('>'), DOWN('v'), LEFT('<');

    final char c;

    WalkDirection(char c) {
        this.c = c;
    }

    public static WalkDirection of(char c) {
        return Arrays.stream(values())
                .filter(value -> value.c == c)
                .findFirst()
                .orElse(null);
    }
}

enum WalkResult {
    IN_PROGRESS, END, LOOP_DETECTED
}
