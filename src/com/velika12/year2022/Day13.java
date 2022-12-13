package com.velika12.year2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day13 {

    private static List<Object> parsePacket(String packet) {
        List<Character> list = packet.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        return (List<Object>)(parseList(list.iterator()).get(0));
    }

    private static List<Object> parseList(Iterator<Character> packetIterator) {
        List<Object> list = new ArrayList<>();

        String number = "";
        while (packetIterator.hasNext()) {
            char current = packetIterator.next();
            if (current == '[') {
                list.add(parseList(packetIterator));
            } else if (current == ']') {
                if (!number.isEmpty()) {
                    list.add(Integer.parseInt(number));
                }
                return list;
            } else if (current == ',') {
                if (!number.isEmpty()) {
                    list.add(Integer.parseInt(number));
                }
                number = "";
            } else {
                number += current;
            }
        }

        return list;
    }

    private static int compare(List<Object> packet1, List<Object> packet2) {
        if (packet1.isEmpty() && !packet2.isEmpty()) {
            return -1;
        }

        int i = 0;
        while (i < packet1.size()) {
            if (i == packet2.size()) {
                return 1;
            }

            Object left = packet1.get(i);
            Object right = packet2.get(i);

            if (left instanceof Integer && right instanceof Integer) {
                if ((int) left > (int) right) {
                    return 1;
                } else if ((int) left < (int) right) {
                    return -1;
                }
            } else if (left instanceof List && right instanceof List) {
                int result = compare((List)left, (List)right);
                if (result != 0) {
                    return result;
                }
            } else {
                if (left instanceof Integer) {
                    int result = compare(Collections.singletonList(left), (List)right);
                    if (result != 0) {
                        return result;
                    }
                } else {
                    int result = compare((List)left, Collections.singletonList(right));
                    if (result != 0) {
                        return result;
                    }
                }
            }

            i++;

        }

        if (packet1.size() != packet2.size()) {
            return -1;
        }

        return 0;
    }

    public static void main(String[] args) throws FileNotFoundException {
        int sum = 0;
        List<Object> dividerPacket1 = Collections.singletonList(Collections.singletonList(2));
        List<Object> dividerPacket2 = Collections.singletonList(Collections.singletonList(6));
        List<List<Object>> allPackets = new ArrayList<>();
        allPackets.add(dividerPacket1);
        allPackets.add(dividerPacket2);

        try (Scanner scanner = new Scanner(new File("input/year2022/input_day13.txt"))) {
            int pairIndex = 0;

            while (scanner.hasNextLine()) {
                pairIndex++;

                List<Object> packet1 = parsePacket(scanner.nextLine());
                List<Object> packet2 = parsePacket(scanner.nextLine());

                System.out.println(packet1);
                System.out.println(packet2);

                if (compare(packet1, packet2) < 0) {
                    System.out.println("true");
                    sum += pairIndex;
                } else {
                    System.out.println("false");
                }

                allPackets.add(packet1);
                allPackets.add(packet2);

                System.out.println();

                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }
        }

        System.out.println("Sum of indices of packet pairs in right order: " + sum);

        Collections.sort(allPackets, Day13::compare);

        allPackets.forEach(System.out::println);

        int decoderKey = (allPackets.indexOf(dividerPacket1) + 1) * (allPackets.indexOf(dividerPacket2) + 1);
        System.out.println("Decoder key: " + decoderKey);
    }
}
