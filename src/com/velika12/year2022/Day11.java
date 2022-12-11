package com.velika12.year2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day11 {

    private static final Pattern monkeyPattern = Pattern.compile("Monkey (\\d+):");
    private static final Pattern itemsPattern = Pattern.compile("\\s+ Starting items: ((\\d+,*\\s*)+)");
    private static final Pattern operationPattern = Pattern.compile("\\s+ Operation: new = old ([*+]) (\\d+|old)");
    private static final Pattern testPattern = Pattern.compile("\\s+ Test: divisible by (\\d+)");
    private static final Pattern onTestPattern = Pattern.compile("\\s+ If (\\w{4,5}): throw to monkey (\\d+)");

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        Map<Integer, Monkey> monkeys = new LinkedHashMap<>();

        try (Scanner scanner = new Scanner(new File("input/year2022/input_day11.txt"))) {
            int monkeyId = 0;
            List<Item> startingItems = null;
            Consumer<Item> operation = null;
            int testNumber = 0;
            int throwToOnTestSuccess = 0;
            int throwToOnTestFail = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Matcher monkeyMatcher = monkeyPattern.matcher(line);
                if (monkeyMatcher.find()) {
                    monkeyId = Integer.parseInt(monkeyMatcher.group(1));
                    continue;
                }

                Matcher itemsMatcher = itemsPattern.matcher(line);
                if (itemsMatcher.find()) {
                    startingItems = Arrays.stream(itemsMatcher.group(1).split(", ")).map(Integer::new).map(Item::new).collect(Collectors.toList());
                    continue;
                }

                Matcher operationsMatcher = operationPattern.matcher(line);
                if (operationsMatcher.find()) {
                    String operator = operationsMatcher.group(1);
                    try {
                        int operand = Integer.parseInt(operationsMatcher.group(2));
                        if (operator.equals("*")) {
                            operation = i -> i.multiply(operand);
                            continue;
                        }
                        if (operator.equals("+")) {
                            operation = i -> i.plus(operand);
                            continue;
                        }
                    } catch (Exception e) {
                        operation = Item::square;
                        continue;
                    }
                }

                Matcher testMatcher = testPattern.matcher(line);
                if (testMatcher.find()) {
                    testNumber = new Integer(testMatcher.group(1));
                    continue;
                }

                Matcher onTestMatcher = onTestPattern.matcher(line);
                if (onTestMatcher.find()) {
                    boolean result = Boolean.parseBoolean(onTestMatcher.group(1));
                    if (result) {
                        throwToOnTestSuccess = Integer.parseInt(onTestMatcher.group(2));
                    } else {
                        throwToOnTestFail = Integer.parseInt(onTestMatcher.group(2));
                    }

                    continue;
                }

                monkeys.put(monkeyId, new Monkey(monkeyId, startingItems, operation, testNumber, throwToOnTestSuccess, throwToOnTestFail));
            }

            monkeys.put(monkeyId, new Monkey(monkeyId, startingItems, operation, testNumber, throwToOnTestSuccess, throwToOnTestFail));
        }

        for (int round = 1; round <= 10000; round++) {
            monkeys.forEach((id, monkey) -> {
                while (monkey.hasAnythingToPlay()) {
                    int throwToMonkeyId = monkey.inspect();
                    Item thrownItem = monkey.throwItem();
                    monkeys.get(throwToMonkeyId).catchItem(thrownItem);
                }
            });
        }

        System.out.println(monkeys);

        long monkeyBusiness = monkeys.values().stream()
                .sorted((m1, m2) -> m2.getInspectCounter() - m1.getInspectCounter())
                .mapToLong(Monkey::getInspectCounter)
                .limit(2)
                .reduce((i1, i2) -> i1 * i2)
                .getAsLong();

        System.out.println("Monkey business: " + monkeyBusiness);
    }
}

class Monkey {
    private final int id;
    private final LinkedList<Item> items = new LinkedList<>();
    private final Consumer<Item> operation;
    private final int testNumber;
    private final int throwToOnTestSuccess;
    private final int throwToOnTestFail;

    private int inspectCounter = 0;

    public Monkey(int id, List<Item> startingItems, Consumer<Item> operation, Integer testNumber, int throwToOnTestSuccess, int throwToOnTestFail) {
        this.id = id;
        this.items.addAll(startingItems);
        this.operation = operation;
        this.testNumber = testNumber;
        this.throwToOnTestSuccess = throwToOnTestSuccess;
        this.throwToOnTestFail = throwToOnTestFail;

        Item.addPossibleDivider(testNumber);
    }

    public boolean hasAnythingToPlay() {
        return !items.isEmpty();
    }

    public int inspect() {
        Item item = items.getFirst();
        operation.accept(item);
        inspectCounter++;

        return item.isDividedBy(testNumber) ? throwToOnTestSuccess : throwToOnTestFail;
    }

    public Item throwItem() {
        return items.removeFirst();
    }

    public void catchItem(Item item) {
        items.addLast(item);
    }

    public int getInspectCounter() {
        return inspectCounter;
    }

    @Override
    public String toString() {
        return "Monkey{" +
                "id=" + id +
                ", inspectCounter=" + inspectCounter +
                '}';
    }
}

class Item {
    private static final Set<Item> allItems = new HashSet<>();

    public static void addPossibleDivider(int number) {
        Map<Item, Integer> remainderMap = allItems.stream().collect(Collectors.toMap(item -> item, item -> item.initialValue % number));
        remainderMapByDivider.put(number, remainderMap);
    }

    private static final Map<Integer, Map<Item, Integer>> remainderMapByDivider = new HashMap<>();

    private final int initialValue;

    public Item(int number) {
        initialValue = number;
        allItems.add(this);
        remainderMapByDivider.forEach((divider, map) -> map.put(this, number % divider));
    }

    public void plus(int number) {
        remainderMapByDivider.forEach((divider, map) -> map.put(this, (map.get(this) + number) % divider));
    }

    public void multiply(int number) {
        remainderMapByDivider.forEach((divider, map) -> map.put(this, (map.get(this) * number) % divider));
    }
    
    public void square() {
        remainderMapByDivider.forEach((divider, map) -> map.put(this, (map.get(this) * map.get(this)) % divider));
    }
    
    public boolean isDividedBy(int divider) {
        int remainder = remainderMapByDivider.get(divider).get(this);
        return remainder % divider == 0;
    }
}
