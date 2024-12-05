package com.velika12.year2024;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day5 {

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input\\year2024\\input_day5.txt"))) {

            Map<String, Rules> pageRules = new HashMap<>();

            // read rules
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();

                if (str.isEmpty()) {
                    break;
                }

                String[] s = str.split("\\|");
                String leftPage = s[0];
                String rightPage = s[1];

                Rules leftPageRules = pageRules.get(leftPage);
                if (leftPageRules == null) {
                    leftPageRules = new Rules();
                    pageRules.put(leftPage, leftPageRules);
                }
                leftPageRules.right.add(rightPage);

                Rules rightPageRules = pageRules.get(rightPage);
                if (rightPageRules == null) {
                    rightPageRules = new Rules();
                    pageRules.put(rightPage, rightPageRules);
                }
                rightPageRules.left.add(leftPage);
            }

            pageRules.forEach((key, value) -> System.out.println(key + ": " + value));

            // read updates
            int sum = 0;
            int fixedSum = 0;

            while (scanner.hasNextLine()) {
                boolean fixed = false;
                String str = scanner.nextLine();

                String[] s = str.split(",");

                for (int i = 1; i < s.length; i++) {
                    String prev = s[i-1];
                    String curr = s[i];

                    int j = i;

                    while ((!pageRules.get(prev).right.contains(curr) || !pageRules.get(curr).left.contains(prev)) && j > 0) {
                        fixed = true;

                        s[j-1] = curr;
                        s[j] = prev;

                        j--;

                        if (j > 0) {
                            prev = s[j - 1];
                            curr = s[j];
                        }
                    }
                }

                if (fixed) {
                    fixedSum += Integer.parseInt(s[s.length / 2]);
                } else {
                    sum += Integer.parseInt(s[s.length / 2]);
                }
            }

            System.out.println("Sum: " + sum);
            System.out.println("Fixed sum: " + fixedSum);
        }
    }


}

class Rules {
    Set<String> left = new HashSet<>();
    Set<String> right = new HashSet<>();

    @Override
    public String toString() {
        return "Rules{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
