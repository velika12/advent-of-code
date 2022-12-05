package com.velika12.year2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 {

    private static final List<LinkedList<Character>> stacks9000 = new ArrayList<>();
    private static final List<LinkedList<Character>> stacks9001 = new ArrayList<>();

    private static void initStacks(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            char[] array = line.toCharArray();

            if (array.length == 0) {
                break;
            }

            for (int i = 1, j = 0; i < array.length; i += 4, j++) {

                if (stacks9000.size() < j + 1) {
                    stacks9000.add(new LinkedList<>());
                    stacks9001.add(new LinkedList<>());
                }

                char crate = array[i];
                if (Character.isLetter(crate)) {
                    stacks9000.get(j).addLast(crate);
                    stacks9001.get(j).addLast(crate);
                }
            }
        }
    }

    private static void moveCratesBy9000(int numberToMove, int fromIndex, int toIndex) {
        for (int i = 0; i < numberToMove; i++) {
            char targetCrate = stacks9000.get(fromIndex).pop();
            stacks9000.get(toIndex).push(targetCrate);
        }
    }

    private static void moveCratesBy9001(int numberToMove, int fromIndex, int toIndex) {
        LinkedList<Character> crateMoverStack = new LinkedList<>();
        for (int i = 0; i < numberToMove; i++) {
            char targetCrate = stacks9001.get(fromIndex).pop();
            crateMoverStack.push(targetCrate);
        }

        for (int i = 0; i < numberToMove; i++) {
            char targetCrate = crateMoverStack.pop();
            stacks9001.get(toIndex).push(targetCrate);
        }
    }

    private static String getTopStacksMessage(List<LinkedList<Character>> stacks) {
        return stacks.stream()
                .map(LinkedList::pop)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File("input/year2022/input_day5.txt"))) {
            initStacks(scanner);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                Pattern pattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    int numberToMove = Integer.parseInt(matcher.group(1));
                    int fromIndex = Integer.parseInt(matcher.group(2)) - 1;
                    int toIndex = Integer.parseInt(matcher.group(3)) - 1;

                    moveCratesBy9000(numberToMove, fromIndex, toIndex);
                    moveCratesBy9001(numberToMove, fromIndex, toIndex);
                }
            }

            System.out.println("Moved by 9000: " + getTopStacksMessage(stacks9000));
            System.out.println("Moved by 9001: " + getTopStacksMessage(stacks9001));
        }
    }
}
