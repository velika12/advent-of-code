package com.velika12.year2022;

import java.io.*;
import java.util.*;

public class Day6 {

    private static final Map<Character, Integer> packetMarkerCounterMap = new HashMap<>();
    private static final Map<Character, Integer> messageMarkerCounterMap = new HashMap<>();

    private static final LinkedList<Character> packetMarker = new LinkedList<>();
    private static final LinkedList<Character> messageMarker = new LinkedList<>();

    private static int startOfPacketCounter = 0;
    private static int startOfMessageCounter = 0;

    private static boolean findMarker(char character, LinkedList<Character> marker, Map<Character, Integer> markerCounterMap, int numOfDistinctCharacters) {
        marker.addLast(character);

        if (markerCounterMap.containsKey(character)) {
            int count = markerCounterMap.get(character);
            markerCounterMap.put(character, ++count);
        } else {
            markerCounterMap.put(character, 1);
        }

        if (marker.size() == numOfDistinctCharacters) {
            boolean markerExists = markerCounterMap.values().stream().allMatch(c -> c < 2);
            if (markerExists) {
                return true;
            }

            Character characterToRemove = marker.removeFirst();
            int count = markerCounterMap.get(characterToRemove);
            markerCounterMap.put(characterToRemove, --count);
        }

        return false;
    }

    private static boolean findStartOfPacketMarker(char character) {
        startOfPacketCounter++;
        return findMarker(character, packetMarker, packetMarkerCounterMap, 4);
    }

    private static boolean findStartOfMessageMarker(char character) {
        startOfMessageCounter++;
        return findMarker(character, messageMarker, messageMarkerCounterMap, 14);
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("input/year2022/input_day6.txt"))) {

            int symbol;
            boolean foundStartOfPacketMarker = false;
            boolean foundStartOfMessageMarker = false;

            while ((symbol = bufferedReader.read()) != -1) {
                char character = (char) symbol;

                if (!foundStartOfPacketMarker) {
                    foundStartOfPacketMarker = findStartOfPacketMarker(character);
                }

                if (!foundStartOfMessageMarker) {
                    foundStartOfMessageMarker = findStartOfMessageMarker(character);
                }
            }

            System.out.println("Number of processed characters before start-of-packet: " + startOfPacketCounter);
            System.out.println("Number of processed characters before start-of-message: " + startOfMessageCounter);
        }
    }
}
