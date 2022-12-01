package com.velika12;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Main {

    private static final LinkedList<Long> top3ElfCalories = LongStream.of(0, 0, 0).boxed().collect(Collectors.toCollection(LinkedList::new));

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("src\\com\\velika12\\input.txt"))) {

            long elfCalories = 0;

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();

                try {
                    int itemCalories = Integer.parseInt(str);
                    elfCalories += itemCalories;
                } catch (Exception e) {
                    updateTopIfNecessary(elfCalories);
                    elfCalories = 0;
                }
            }

            updateTopIfNecessary(elfCalories);

            System.out.println(top3ElfCalories);
            System.out.println("Max elf calories: " + top3ElfCalories.getFirst());

            long sumOfTop3ElfCalories = top3ElfCalories.stream().mapToLong(Long::longValue).sum();

            System.out.println("Sum of top 3 elf calories: " + sumOfTop3ElfCalories);
        }
    }

    private static void updateTopIfNecessary(long elfCalories) {
        for (int i = 0; i < top3ElfCalories.size(); i++) {
            if (elfCalories > top3ElfCalories.get(i)) {
                top3ElfCalories.add(i, elfCalories);
                top3ElfCalories.removeLast();
                break;
            }
        }
    }
}
