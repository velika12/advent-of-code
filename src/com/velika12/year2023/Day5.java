package com.velika12.year2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.LongStream;

public class Day5 {

    private static final Map<String, List<MappingRule>> rulesMap = new LinkedHashMap<>();

    static {
        rulesMap.put("seed-to-soil map:", new ArrayList<>());
        rulesMap.put("soil-to-fertilizer map:", new ArrayList<>());
        rulesMap.put("fertilizer-to-water map:", new ArrayList<>());
        rulesMap.put("water-to-light map:", new ArrayList<>());
        rulesMap.put("light-to-temperature map:", new ArrayList<>());
        rulesMap.put("temperature-to-humidity map:", new ArrayList<>());
        rulesMap.put("humidity-to-location map:", new ArrayList<>());
    }

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input\\year2023\\input_day5.txt"))) {

            String str = scanner.nextLine();
            str = str.replace("seeds: ", "");
            List<Long> seeds = Arrays.stream(str.split(" ")).map(Long::parseLong).toList();

            List<MappingRule> currentRules = null;

            while (scanner.hasNextLine()) {
                str = scanner.nextLine();
                if (str.isEmpty()) {
                    currentRules = null;
                    continue;
                }
                if (str.endsWith("map:")) {
                    currentRules = rulesMap.get(str);
                    continue;
                }
                currentRules.add(new MappingRule(str.split(" ")));
            }

            System.out.println(rulesMap);

            long minLocation = seeds.stream()
                    .map(Day5::findLocation)
                    .min(Long::compareTo)
                    .get();

            System.out.println("Min location: " + minLocation);

            long minLocationForRanges = Long.MAX_VALUE;
            for (int i = 0; i < seeds.size(); i += 2) {
                System.out.println("------");
                System.out.println("i = " + i);
                System.out.println("------");
                long minLocationForRange = findMinLocation(seeds.get(i), seeds.get(i+1));
                minLocationForRanges = Math.min(minLocationForRanges, minLocationForRange);
            }

            System.out.println("Min location for ranges: " + minLocationForRanges);
        }
    }

    private static long findMinLocation(long startSeed, long range) {
        return LongStream.range(startSeed, startSeed + range + 1).parallel().map(Day5::findLocation).min().getAsLong();
    }

    private static long findLocation(long seed) {
        long target = seed;

        for (List<MappingRule> rules : rulesMap.values()) {
            target = findMappedValue(target, rules);
        }

        return target;
    }

    private static long findMappedValue(long target, List<MappingRule> rules) {
        for (MappingRule rule : rules) {
            if (target < rule.start2 || target > rule.start2 + rule.range) {
                continue;
            }

            return rule.start1 + (target - rule.start2);
        }

        return target;
    }
}

class MappingRule {
    long start1, start2, range;

    public MappingRule(String[] values) {
        this.start1 = Long.parseLong(values[0]);
        this.start2 = Long.parseLong(values[1]);
        this.range = Long.parseLong(values[2]);
    }

    @Override
    public String toString() {
        return "MappingRule{" +
                "start1=" + start1 +
                ", start2=" + start2 +
                ", range=" + range +
                '}';
    }
}