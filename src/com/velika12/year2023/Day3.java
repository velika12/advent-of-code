package com.velika12.year2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day3 {

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input\\year2023\\input_day3.txt"))) {

            List<Number> numbers = new ArrayList<>();
            List<Symbol> symbols  = new ArrayList<>();

            int y = 0;

            while (scanner.hasNextLine()) {
                char[] cArr = scanner.nextLine().toCharArray();
                Number currentNumber = null;

                for (int x = 0; x < cArr.length; x++) {
                    char c = cArr[x];

                    if (Character.isDigit(c)) {
                        if (currentNumber == null) {
                            currentNumber = new Number(x, y);
                        }
                        currentNumber.fillValue(c);
                        currentNumber.x2 = x;
                        currentNumber.y2 = y;

                        continue;
                    }
                    if (c != '.') {
                        symbols.add(new Symbol(x, y, c));
                    }

                    if (currentNumber != null) {
                        numbers.add(currentNumber);
                        currentNumber = null;
                    }
                }

                if (currentNumber != null) {
                    numbers.add(currentNumber);
                }

                y++;
            }

            System.out.println(numbers);
            System.out.println(symbols);

            int sum = numbers.stream()
                    .filter(number -> isPartNumber(number, symbols))
                    .map(Number::getInt)
                    .reduce(0, Integer::sum);

            System.out.println("Sum of Part numbers: " + sum);

            long gearSum = symbols.stream()
                    .map(symbol -> calculateGearRatio(symbol, numbers))
                    .reduce(0L, Long::sum);

            System.out.println("Sum of Gear ratios: " + gearSum);
        }
    }

    private static boolean isPartNumber(Number number, List<Symbol> symbols) {
        for (Symbol symbol : symbols) {
            if (isAdjacent(number, symbol)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAdjacent(Number number, Symbol symbol) {
        if (Math.abs(symbol.y - number.y1) <= 1 && Math.abs(symbol.y - number.y2) <= 1) {
            if (symbol.x >= number.x1 - 1 && symbol.x <= number.x2 + 1) {
                return true;
            }
        }

        return false;
    }

    private static long calculateGearRatio(Symbol symbol, List<Number> numbers) {
        if (symbol.c != '*') {
            return 0;
        }

        List<Number> adjacentNumbers = numbers.stream()
                .filter(number -> isAdjacent(number, symbol))
                .toList();

        if (adjacentNumbers.size() == 2) {
            return (long) adjacentNumbers.get(0).getInt() * adjacentNumbers.get(1).getInt();
        }

        return 0;
    }
}

class Number {
    int x1, y1, x2, y2;

    StringBuilder valueBuilder = new StringBuilder();

    public Number(int x1, int y1) {
        this.x1 = x1;
        this.y1 = y1;
    }

    public void fillValue(char c) {
        valueBuilder.append(c);
    }

    public int getInt() {
        return Integer.parseInt(valueBuilder.toString());
    }

    @Override
    public String toString() {
        return "({" + x1 + "," + y1 + "}, " + "{" + x2 + "," + y2 + "}, " + "{" + valueBuilder.toString() + "})";
    }
}

class Symbol {
    int x, y;
    char c;

    public Symbol(int x, int y, char c) {
        this.x = x;
        this.y = y;
        this.c = c;
    }

    @Override
    public String toString() {
        return "({" + x + "," + y + "}, " + "{" + c + "})";
    }
}
