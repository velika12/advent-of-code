package com.velika12.year2022;

import com.velika12.utils.Grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day14 {

    public static void main(String[] args) throws FileNotFoundException {
        List<List<Point>> paths = new ArrayList<>();
        Integer minX = null;
        Integer maxX = null;
        Integer minY = 0;
        Integer maxY = null;

        Point source = new Point(500, 0);

        try (Scanner scanner = new Scanner(new File("input/year2022/input_day14.txt"))) {
            while (scanner.hasNextLine()) {
                List<Point> path = new ArrayList<>();

                String line = scanner.nextLine();
                String[] coordinates = line.split(" -> ");
                for (String pointCoordinate : coordinates) {
                    String[] xy = pointCoordinate.split(",");
                    Point point = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
                    path.add(point);

                    minX = minX == null ? point.x : point.x < minX ? point.x : minX;
                    maxX = maxX == null ? point.x : point.x > maxX ? point.x : maxX;
                    maxY = maxY == null ? point.y : point.y > maxY ? point.y : maxY;
                }

                paths.add(path);
            }
        }

        simulation1(minX, maxX, minY, maxY, paths, source).print();
        System.out.println();
        simulation2(minX, maxX, minY, maxY, paths, source).print();
    }

    private static Grid simulation1(int minX, int maxX, int minY, int maxY, List<List<Point>> paths, Point source) {
        Grid grid = new Grid(minX, maxX, minY, maxY);
        return simulation(grid, paths, source);
    }

    private static Grid simulation2(int minX, int maxX, int minY, int maxY, List<List<Point>> paths, Point source) {
        minX -= 500;
        maxX += 500;
        maxY += 2;

        Grid grid = new Grid(minX, maxX, minY, maxY);
        for (int x = minX; x < maxX + 1; x++) {
            putRock(grid, x, maxY);
        }
        return simulation(grid, paths, source);
    }

    private static Grid simulation(Grid grid, List<List<Point>> paths, Point source) {
        for (List<Point> path : paths) {
            for (int i = 0; i < path.size() - 1; i++) {
                Point point1 = path.get(i);
                Point point2 = path.get(i+1);

                if (point1.y == point2.y) {
                    int pathMinX = Math.min(point1.x, point2.x);
                    int pathMaxX = Math.max(point1.x, point2.x);

                    for (int j = 0; j < pathMaxX - pathMinX + 1; j++) {
                        putRock(grid,pathMinX + j, point1.y);
                    }
                }

                if (point1.x == point2.x) {
                    int pathMinY = Math.min(point1.y, point2.y);
                    int pathMaxY = Math.max(point1.y, point2.y);

                    for (int j = 0; j < pathMaxY - pathMinY + 1; j++) {
                        putRock(grid, point1.x, pathMinY + j);
                    }
                }
            }
        }

        putSource(grid, source.x, source.y);

        int sandCounter = 0;

        while (!isSand(grid, source) && !fall(source, grid)) {
            sandCounter++;
        }

        System.out.println("Units of sand came to rest: " + sandCounter);
        System.out.println();

        return grid;
    }

    private static boolean isSand(Grid grid, Point point) {
        return grid.get(point.x, point.y) == 'o';
    }

    private static boolean isAir(Grid grid, Point point) {
        return grid.get(point.x, point.y) == '.';
    }

    private static boolean isSource(Grid grid, Point point) {
        return grid.get(point.x, point.y) == '+';
    }

    private static boolean isAbyss(Grid grid, Point point) {
        return grid.isOutside(point.x, point.y);
    }

    private static void putRock(Grid grid, int x, int y) {
        grid.put(x, y, '#');
    }

    private static void putSource(Grid grid, int x, int y) {
        grid.put(x, y, '+');
    }

    private static void putAir(Grid grid, int x, int y) {
        grid.put(x, y, '.');
    }

    private static void putSand(Grid grid, int x, int y) {
        grid.put(x, y, 'o');
    }

    private static boolean fall(Point current, Grid grid) {

        // check down
        Point next = new Point(current.x, current.y + 1);

        if (isAbyss(grid, next)) {
            if (!isSource(grid, current)) {
                putAir(grid, current.x, current.y);
            }
            return true;
        }

        if (isAir(grid, next)) {
            move(grid, current, next);
            return fall(next, grid);
        }

        // check down left
        next = new Point(current.x - 1, current.y + 1);

        if (isAbyss(grid, next)) {
            if (!isSource(grid, current)) {
                putAir(grid, current.x, current.y);
            }
            return true;
        }

        if (isAir(grid, next)) {
            move(grid, current, next);
            return fall(next, grid);
        }

        // check down right
        next = new Point(current.x + 1, current.y + 1);

        if (isAbyss(grid, next)) {
            if (!isSource(grid, current)) {
                putAir(grid, current.x, current.y);
            }
            return true;
        }

        if (isAir(grid, next)) {
            move(grid, current, next);
            return fall(next, grid);
        }

        // nowhere to fall, coming to rest

        if (isSource(grid, current)) {
            putSand(grid, current.x, current.y);
        }

        return false;
    }

    private static void move(Grid grid, Point current, Point next) {
        putSand(grid, next.x, next.y);
        if (!isSource(grid, current)) {
            putAir(grid, current.x, current.y);
        }
    }
}

class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}