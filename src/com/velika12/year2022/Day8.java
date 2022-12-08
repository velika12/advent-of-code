package com.velika12.year2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day8 {

    private static int examine(Tree targetTree, List<Tree> trees) {
        int counter = 0;
        boolean isVisible = true;

        for (Tree tree : trees) {
            counter++;
            if (tree.getHeight() >= targetTree.getHeight()) {
                isVisible = false;
                break;
            }
        }

        if (isVisible) {
            targetTree.setVisible();
        }

        return counter;
    }

    private static List<Tree> sliceUp(List<List<Tree>> grid, int targetI, int targetJ) {
        List<Tree> slice = new ArrayList<>();

        for (int i = targetI - 1; i >= 0; i--) {
            slice.add(grid.get(i).get(targetJ));
        }

        return slice;
    }

    private static List<Tree> sliceDown(List<List<Tree>> grid, int targetI, int targetJ) {
        List<Tree> slice = new ArrayList<>();
        for (int i = targetI + 1; i < grid.size(); i++) {
            slice.add(grid.get(i).get(targetJ));
        }

        return slice;
    }

    private static List<Tree> sliceRight(List<List<Tree>> grid, int targetI, int targetJ) {
        List<Tree> slice = new ArrayList<>();
        for (int j = targetJ + 1; j < grid.get(targetI).size(); j++) {
            slice.add(grid.get(targetI).get(j));
        }

        return slice;
    }

    private static List<Tree> sliceLeft(List<List<Tree>> grid, int targetI, int targetJ) {
        List<Tree> slice = new ArrayList<>();
        for (int j = targetJ - 1; j >= 0; j--) {
            slice.add(grid.get(targetI).get(j));
        }

        return slice;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<List<Tree>> grid = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("input/year2022/input_day8.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                List<Tree> row = Arrays.stream(line.split("\\B"))
                        .map(Integer::valueOf)
                        .map(Tree::new)
                        .collect(Collectors.toList());

                grid.add(row);
            }
        }

        int maxScore = 0;

        for (int i = 0; i < grid.size(); i++) {
            List<Tree> row = grid.get(i);
            for (int j = 0; j < row.size(); j++) {
                Tree targetTree = row.get(j);

                int score = examine(targetTree, sliceUp(grid, i, j))
                    * examine(targetTree, sliceRight(grid, i, j))
                    * examine(targetTree, sliceLeft(grid, i, j))
                    * examine(targetTree, sliceDown(grid, i, j));

                maxScore = Math.max(score, maxScore);
            }
        }

        for (List<Tree> row : grid) {
            System.out.println(row);
        }

        System.out.println();

        System.out.println("Number of visible trees: " + Tree.getVisibleCounter());
        System.out.println("Highest scenic score: " + maxScore);
    }
}

class Tree {
    private static int visibleCounter = 0;

    public static int getVisibleCounter() {
        return visibleCounter;
    }

    private final int height;
    private boolean visible;

    public Tree(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setVisible() {
        if (!visible) {
            this.visible = true;
            visibleCounter++;
        }
    }

    @Override
    public String toString() {
        return "Tree{" +
                "height=" + height +
                ", visible=" + visible +
                '}';
    }
}
