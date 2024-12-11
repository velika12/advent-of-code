package com.velika12.year2024;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day7 {

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input\\year2024\\input_day7.txt"))) {

            long sum2 = 0;
            long sum3 = 0;

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                String[] s = str.split(": ");
                long total = Long.parseLong(s[0]);
                List<Long> numbers = Arrays.stream(s[1].split(" ")).map(Long::parseLong).toList();

                if (checkEquation2(total, numbers)) {
                    sum2 += total;
                }
                if (checkEquation3(total, numbers)) {
                    sum3 += total;
                }
            }

            System.out.println("Sum2: " + sum2);
            System.out.println("Sum3: " + sum3);
        }
    }

    private static boolean checkEquation2(long total, List<Long> numbers) {
        int combinationsCount = (int) Math.pow(2, numbers.size() - 1);
        for (int i = 0; i < combinationsCount; i++) {
            long result = numbers.get(0);
            for (int k = 0; k < numbers.size() - 1; k++) {
                int bit = (i / (int) Math.pow(2, k)) % 2; //(i >> k) & 1;
                if (bit == 0) {
                    result *= numbers.get(k+1);
                } else {
                    result += numbers.get(k+1);
                }
            }

            if (result == total) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkEquation3(long total, List<Long> numbers) {
        int combinationsCount = (int) Math.pow(3, numbers.size() - 1);
        for (int i = 0; i < combinationsCount; i++) {
            long result = numbers.get(0);
            for (int k = 0; k < numbers.size() - 1; k++) {
                int bit = (i / (int) Math.pow(3, k)) % 3;
                if (bit == 0) {
                    result *= numbers.get(k+1);
                } else if (bit == 1) {
                    result += numbers.get(k+1);
                } else {
                    result = Long.parseLong(result + String.valueOf(numbers.get(k+1)));
                }
            }

            if (result == total) {
                return true;
            }
        }

        return false;
    }
}
