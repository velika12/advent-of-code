package com.velika12.year2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day3 {

    private static char findFailedItemType(char[] rucksack) {
        Set<Character> itemTypesInFirstCompartment = new HashSet<>();

        for (int i = 0; i < rucksack.length; i++) {
            if (i < rucksack.length / 2) {
                itemTypesInFirstCompartment.add(rucksack[i]);
            } else {
                if (itemTypesInFirstCompartment.contains(rucksack[i])) {
                    return rucksack[i];
                }
            }
        }

        throw new IllegalArgumentException("No failed items in rucksack");
    }

    private static char findBadgeType(List<Set<Character>> group) {
        Set<Character> badgeCandidates = group.get(0);
        badgeCandidates.retainAll(group.get(1));
        badgeCandidates.retainAll(group.get(2));

        if (badgeCandidates.size() != 1) {
            throw new IllegalArgumentException("Invalid rucksack content in group");
        }

        return badgeCandidates.stream().findFirst().get();
    }

    private static int getItemTypePriority(char itemType) {
        if (Character.isLowerCase(itemType)) {
            return (itemType - 'a' + 1);
        } else {
            return (itemType - 'A' + 1) + ('z' - 'a' + 1);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File("input\\year2022\\input_day3.txt"))) {

            long totalFailedPriority = 0;
            long totalBadgesPriority = 0;
            int elfInGroupCounter = 0;

            List<Set<Character>> group = Arrays.asList(new HashSet<>(), new HashSet<>(), new HashSet<>());

            while (scanner.hasNextLine()) {
                String line = scanner.next();
                char[] rucksack = line.toCharArray();

                char failedItemType = findFailedItemType(rucksack);
                totalFailedPriority += getItemTypePriority(failedItemType);

                for (char item : rucksack) {
                    Set<Character> elfItemTypes = group.get(elfInGroupCounter);
                    elfItemTypes.add(item);
                }

                elfInGroupCounter++;

                if (elfInGroupCounter == 3) {

                    char badgeItemType = findBadgeType(group);
                    totalBadgesPriority += getItemTypePriority(badgeItemType);

                    for (Set<Character> elfItemTypes : group) {
                        elfItemTypes.clear();
                    }

                    elfInGroupCounter = 0;
                }
            }

            System.out.println("Total failed priority: " + totalFailedPriority);
            System.out.println("Total badges priority: " + totalBadgesPriority);
        }
    }
}
