package com.velika12.year2024;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day2 {

    public static void main(String[] args) throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File("input\\year2024\\input_day2.txt"))) {

            int safeCounter = 0;
            int safeWithToleranceCounter = 0;

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                List<Integer> report = Arrays.stream(str.split(" ")).map(Integer::parseInt).toList();
                if (isReportSafe(report)) {
                    safeCounter++;
                }
                if (isReportSafeWithTolerance(report)) {
                    safeWithToleranceCounter++;
                }
            }

            System.out.println("Safe counter: " + safeCounter);
            System.out.println("Safe with tolerance counter: " + safeWithToleranceCounter);
        }
    }

    private static boolean isReportSafe(List<Integer> report) {
        Boolean increasingFlag = null;
        for (int i = 1; i < report.size(); i++) {
            int prev = report.get(i - 1);
            int curr = report.get(i);
            int diff = curr - prev;

            if (unsafe(diff, increasingFlag)) {
                return false;
            }

            if (increasingFlag == null) {
                increasingFlag = diff > 0;
            }
        }

        return true;
    }

    private static boolean isReportSafeWithTolerance(List<Integer> report) {
        Boolean increasingFlag = null;
        for (int i = 1; i < report.size(); i++) {
            int prev = report.get(i - 1);
            int curr = report.get(i);
            int diff = curr - prev;

            if (unsafe(diff, increasingFlag)) {
                List<Integer> fixedReportNoCurr = fix(report, i);
                if (isReportSafe(fixedReportNoCurr)) {
                    return true;
                }

                List<Integer> fixedReportNoPrev = fix(report, i - 1);
                if (isReportSafe(fixedReportNoPrev)) {
                    return true;
                }

                if (increasingFlag != null) {
                    List<Integer> fixedReportNoPrevPrev = fix(report, i - 2);
                    if (isReportSafe(fixedReportNoPrevPrev)) {
                        return true;
                    }
                }

                return false;
            }

            if (increasingFlag == null) {
                increasingFlag = diff > 0;
            }
        }

        return true;
    }

    private static boolean unsafe(int diff, Boolean increasingFlag) {
        return diff == 0 ||
                Math.abs(diff) > 3 ||
                diff > 0 && Boolean.FALSE.equals(increasingFlag) ||
                diff < 0 && Boolean.TRUE.equals(increasingFlag);
    }

    private static List<Integer> fix(List<Integer> report, int i) {
        List<Integer> fixedReport = new ArrayList<>(report);
        fixedReport.remove(i);
        return fixedReport;
    }
}
