package com.velika12.year2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day7 {

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input\\year2023\\input_day7.txt"))) {
            List<Hand1> hands1 = new ArrayList<>();
            List<Hand2> hands2 = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                String[] parts = str.split(" ");
                hands1.add(new Hand1(parts));
                hands2.add(new Hand2(parts));
            }

            System.out.println(hands1);
            System.out.println(hands2);

            hands1.sort(Comparator.reverseOrder());
            hands2.sort(Comparator.reverseOrder());

            System.out.println(hands1);
            System.out.println(hands2);

            long totalWinnings1 = 0;
            long totalWinnings2 = 0;

            for (int rank = 1; rank <= hands1.size(); rank++) {
                totalWinnings1 += ((long) rank * hands1.get(rank - 1).bid);
                totalWinnings2 += ((long) rank * hands2.get(rank - 1).bid);
            }

            System.out.println("#1 Total winnings: " + totalWinnings1);
            System.out.println("#2 Total winnings: " + totalWinnings2);
        }
    }
}

class Hand2 implements Comparable<Hand2> {
    private static Map<Character, Integer> strengthMap = new HashMap<>();
    static {
        strengthMap.put('J', 1);
        strengthMap.put('2', 2);
        strengthMap.put('3', 3);
        strengthMap.put('4', 4);
        strengthMap.put('5', 5);
        strengthMap.put('6', 6);
        strengthMap.put('7', 7);
        strengthMap.put('8', 8);
        strengthMap.put('9', 9);
        strengthMap.put('T', 10);
        strengthMap.put('Q', 12);
        strengthMap.put('K', 13);
        strengthMap.put('A', 14);
    }

    char[] value;
    int bid;
    HandType type;

    public Hand2(String[] parts) {
        this.value = parts[0].toCharArray();
        this.bid = Integer.parseInt(parts[1]);
        this.type = detectType(value);
    }

    private HandType detectType(char[] cArr) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : cArr) {
            Integer counter = map.get(c);
            map.put(c, counter == null ? 1 : counter + 1);
        }

        Integer jokerCounter = map.remove('J');
        if (jokerCounter != null) {
            map.entrySet().stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue))
                    .ifPresent(e -> e.setValue(e.getValue() + jokerCounter));
        }

        if (Integer.valueOf(5).equals(jokerCounter) || map.containsValue(5)) {
            return HandType.FIVE;
        }
        if (map.containsValue(4)) {
            return HandType.FOUR;
        }
        if (map.containsValue(3) && map.containsValue(2)) {
            return HandType.FULL_HOUSE;
        }
        if (map.containsValue(3)) {
            return HandType.THREE;
        }
        if (map.values().stream().filter(v -> v.equals(2)).count() == 2) {
            return HandType.TWO_PAIR;
        }
        if (map.containsValue(2)) {
            return HandType.ONE_PAIR;
        }
        return HandType.HIGH_CARD;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "value='" + Arrays.toString(value) + '\'' +
                ", bid=" + bid +
                ", type=" + type +
                '}';
    }

    @Override
    public int compareTo(Hand2 o) {
        if (this.type != o.type) {
            return this.type.compareTo(o.type);
        }

        for (int i = 0; i < value.length; i++) {
            char c1 = this.value[i];
            char c2 = o.value[i];

            if (strengthMap.get(c1).equals(strengthMap.get(c2))) {
                continue;
            }

            return strengthMap.get(c2).compareTo(strengthMap.get(c1));
        }

        return 0;
    }
}

class Hand1 implements Comparable<Hand1> {
    private static Map<Character, Integer> strengthMap = new HashMap<>();
    static {
        strengthMap.put('2', 2);
        strengthMap.put('3', 3);
        strengthMap.put('4', 4);
        strengthMap.put('5', 5);
        strengthMap.put('6', 6);
        strengthMap.put('7', 7);
        strengthMap.put('8', 8);
        strengthMap.put('9', 9);
        strengthMap.put('T', 10);
        strengthMap.put('J', 11);
        strengthMap.put('Q', 12);
        strengthMap.put('K', 13);
        strengthMap.put('A', 14);
    }

    char[] value;
    int bid;
    HandType type;

    public Hand1(String[] parts) {
        this.value = parts[0].toCharArray();
        this.bid = Integer.parseInt(parts[1]);
        this.type = detectType(value);
    }

    private HandType detectType(char[] cArr) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : cArr) {
            Integer counter = map.get(c);
            map.put(c, counter == null ? 1 : counter + 1);
        }

        if (map.containsValue(5)) {
            return HandType.FIVE;
        }
        if (map.containsValue(4)) {
            return HandType.FOUR;
        }
        if (map.containsValue(3) && map.containsValue(2)) {
            return HandType.FULL_HOUSE;
        }
        if (map.containsValue(3)) {
            return HandType.THREE;
        }
        if (map.values().stream().filter(v -> v.equals(2)).count() == 2) {
            return HandType.TWO_PAIR;
        }
        if (map.containsValue(2)) {
            return HandType.ONE_PAIR;
        }
        return HandType.HIGH_CARD;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "value='" + Arrays.toString(value) + '\'' +
                ", bid=" + bid +
                ", type=" + type +
                '}';
    }

    @Override
    public int compareTo(Hand1 o) {
        if (this.type != o.type) {
            return this.type.compareTo(o.type);
        }

        for (int i = 0; i < value.length; i++) {
            char c1 = this.value[i];
            char c2 = o.value[i];

            if (strengthMap.get(c1).equals(strengthMap.get(c2))) {
                continue;
            }

            return strengthMap.get(c2).compareTo(strengthMap.get(c1));
        }

        return 0;
    }
}

enum HandType {
    FIVE, FOUR, FULL_HOUSE, THREE, TWO_PAIR, ONE_PAIR, HIGH_CARD
}