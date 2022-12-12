package com.velika12.year2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day12 {

    private static void resetHeightMap(List<List<Square>> heightMap) {
        heightMap.forEach(line -> line.forEach(Square::reset));
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<List<Square>> heightMap = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("input/year2022/input_day12.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                List<Square> heights = line.chars().mapToObj(c -> new Square((char) c)).collect(Collectors.toList());
                heightMap.add(heights);
            }
        }

        Square start = null;
        Set<Square> lowestSquares = new HashSet<>();

        for (int i = 0; i < heightMap.size(); i++) {
            for (int j = 0; j < heightMap.get(i).size(); j++) {
                Square currentSquare = heightMap.get(i).get(j);
                if (currentSquare.isStart()) {
                    start = currentSquare;
                }
                if (currentSquare.isLowest()) {
                    lowestSquares.add(currentSquare);
                }

                if (j < heightMap.get(i).size() - 1) {
                    Square rightSquare = heightMap.get(i).get(j + 1);
                    currentSquare.setPathToAndBack(rightSquare);
                }
                if (i < heightMap.size() - 1) {
                    Square bottomSquare = heightMap.get(i + 1).get(j);
                    currentSquare.setPathToAndBack(bottomSquare);
                }
            }
        }

        int stepsToFinishFromStart = goTo(start.getNeighbours(), 1);
        System.out.println("Min steps to finish from start: " + stepsToFinishFromStart);

        resetHeightMap(heightMap);

        int minStepsToFinishFromAnyLowestSquare = goTo(lowestSquares, 0);
        System.out.println("Min steps to finish from any lowest square: " + minStepsToFinishFromAnyLowestSquare);
    }

    private static int goTo(Set<Square> neighbours, int step) {
        Set<Square> nextLevelNeighbours = new HashSet<>();

        for (Square square : neighbours) {
            if (square.isFinish()) {
                return step;
            }

            if (!square.wasVisited()) {
                square.visit();
                nextLevelNeighbours.addAll(square.getNeighbours());
            }
        }

        return goTo(nextLevelNeighbours, step + 1);
    }
}

class Square {
    private final char height;
    private boolean start;
    private boolean finish;
    private boolean visited;

    private final Set<Square> neighbours = new HashSet<>();

    public Square(char height) {
        if (height == 'S') {
            this.start = true;
            this.height = 'a';
        } else if (height == 'E') {
            this.finish = true;
            this.height = 'z';
        } else {
            this.height = height;
        }
    }

    public boolean isStart() {
        return start;
    }

    public boolean isFinish() {
        return finish;
    }

    public boolean isLowest() {
        return height == 'a';
    }

    public void setPathToAndBack(Square square) {
        if (checkPath(this, square)) {
            this.neighbours.add(square);
        }
        if (checkPath(square, this)) {
            square.neighbours.add(this);
        }
    }

    private boolean checkPath(Square from, Square to) {
        return to.height - from.height <= 1;
    }

    public Set<Square> getNeighbours() {
        return neighbours;
    }

    public boolean wasVisited() {
        return visited;
    }

    public void visit() {
        visited = true;
    }

    public void reset() {
        visited = false;
    }
}
