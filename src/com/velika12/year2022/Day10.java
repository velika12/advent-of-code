package com.velika12.year2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day10 {

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File("input/year2022/input_day10.txt"))) {

            CrtManager crtManager = new CrtManager();
            CpuManager cpuManager = new CpuManager();
            cpuManager.setCrtManager(crtManager);

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();

                if (line.startsWith("addx")) {
                    String[] cmd = line.split(" ");
                    int number = Integer.parseInt(cmd[1]);
                    cpuManager.executeAddx(number);
                } else {
                    cpuManager.executeNoop();
                }
            }

            System.out.println("Total signal strength: " + cpuManager.getTotalSignalStrength());
            System.out.println();

            crtManager.display();
        }
    }
}

class CpuManager {
    private int cycleCounter = 0;
    private int x = 1;
    private int totalSignalStrength = 0;
    private int strengthCheckCounter = 20;

    private CrtManager crtManager;

    public void setCrtManager(CrtManager crtManager) {
        this.crtManager = crtManager;
    }

    public void executeAddx(int number) {
        execute(2, number);
    }

    public void executeNoop() {
        execute(1, 0);
    }

    private void execute(int duration, int value) {
        while (duration > 0) {
            cycleCounter++;

            if (crtManager != null) {
                crtManager.drawPixel(cycleCounter, x);
            }

            if (cycleCounter == strengthCheckCounter) {
                totalSignalStrength += cycleCounter * x;
                strengthCheckCounter += 40;
            }

            duration--;
        }

        x += value;
    }

    public int getTotalSignalStrength() {
        return totalSignalStrength;
    }
}

class CrtManager {
    private static final int SCREEN_WIDTH = 40;
    private static final int SCREEN_HEIGHT = 6;

    private final char[][] screen = new char[SCREEN_HEIGHT][SCREEN_WIDTH];

    public void drawPixel(int cycleCounter, int spriteCenter) {
        int y = (cycleCounter - 1) / SCREEN_WIDTH;
        int x = (cycleCounter - 1) % SCREEN_WIDTH;

        if (isSpriteVisible(x, spriteCenter)) {
            screen[y][x] = '#';
        } else {
            screen[y][x] = '.';
        }
    }

    private boolean isSpriteVisible(int x, int spriteCenter) {
        return Math.abs(spriteCenter - x) < 2;
    }

    public void display() {
        for (char[] row : screen) {
            for (char symbol : row) {
                System.out.print(symbol);
            }
            System.out.println();
        }
    }
}
