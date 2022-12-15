package com.velika12.utils;

import java.util.Arrays;

public class Grid {
    private Integer minX;
    private Integer maxX;
    private Integer minY;
    private Integer maxY;

    private char[][] grid;

    public Grid(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        this.grid = new char[maxY + 1][maxX - minX + 1];
        for (char[] line : grid) {
            Arrays.fill(line, '.');
        }
    }

    public boolean isOutside(int x, int y) {
        return x < minX || x > maxX || y < minY || y > maxY;
    }

    public void put(int x, int y, char c) {
        grid[y - minY][x - minX] = c;
    }

    public char get(int x, int y) {
        return grid[y - minY][x - minX];
    }

    public void print() {
        for (char[] line : grid) {
            for (char c : line) {
                System.out.print(c);
            }
            System.out.println();
        }
    }
}
