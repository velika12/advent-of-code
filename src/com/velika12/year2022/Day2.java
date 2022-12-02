package com.velika12.year2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day2 {

    private static final Outcome[][] rules = {
            { Outcome.DRAW, Outcome.WIN, Outcome.LOSS },
            { Outcome.LOSS, Outcome.DRAW, Outcome.WIN },
            { Outcome.WIN, Outcome.LOSS, Outcome.DRAW }
    };

    private static int getRoundScoreWithFirstStrategy(char[] splittedLine) {
        Shape opponentShape = Shape.getByAbbr(splittedLine[0]);
        Shape personalShape = Shape.getByAbbr(splittedLine[2]);

        Outcome roundResult = rules[opponentShape.getRuleIndex()][personalShape.getRuleIndex()];

        return getRoundScore(personalShape, roundResult);
    }

    private static int getRoundScoreWithSecondStrategy(char[] splittedLine) {
        Shape opponentShape = Shape.getByAbbr(splittedLine[0]);
        Outcome desiredOutcome = Outcome.getByAbbr(splittedLine[2]);

        Shape personalShape = getPersonalShape(opponentShape, desiredOutcome);

        return getRoundScore(personalShape, desiredOutcome);
    }

    private static int getRoundScore(Shape personalShape, Outcome roundResult) {
        return personalShape.getScore() + roundResult.getScore();
    }

    private static Shape getPersonalShape(Shape opponentShape, Outcome desiredOutcome) {
        Outcome[] personalOptions = rules[opponentShape.getRuleIndex()];
        for (int i = 0; i < personalOptions.length; i++) {
            if (personalOptions[i] == desiredOutcome) {
                return Shape.getByRuleIndex(i);
            }
        }

        throw new IllegalStateException("Broken rules");
    }

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File("input\\year2022\\input_day2.txt"))) {

            long totalScoreFirstStrategy = 0;
            long totalScoreSecondStrategy = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                char[] splittedLine = line.toCharArray();

                totalScoreFirstStrategy += getRoundScoreWithFirstStrategy(splittedLine);
                totalScoreSecondStrategy += getRoundScoreWithSecondStrategy(splittedLine);
            }

            System.out.println("First strategy total score: " + totalScoreFirstStrategy);
            System.out.println("Second strategy total score: " + totalScoreSecondStrategy);
        }
    }
}

enum Shape {
    ROCK(0, 'A', 'X'),
    PAPER(1, 'B', 'Y'),
    SCISSORS(2, 'C', 'Z');

    private final int ruleIndex;
    private final char opponentAbbr;
    private final char personalAbbr;

    Shape(int ruleIndex, char opponentAbbr, char personalAbbr) {
        this.ruleIndex = ruleIndex;
        this.opponentAbbr = opponentAbbr;
        this.personalAbbr = personalAbbr;
    }

    public int getRuleIndex() {
        return ruleIndex;
    }

    public int getScore() {
        return ruleIndex + 1;
    }

    public static Shape getByRuleIndex(int ruleIndex) {
        for (Shape shape : values()) {
            if (shape.ruleIndex == ruleIndex) {
                return shape;
            }
        }

        throw new IllegalArgumentException("Unexpected ruleIndex " + ruleIndex);
    }

    public static Shape getByAbbr(char abbr) {
        for (Shape shape : values()) {
            if (shape.opponentAbbr == abbr || shape.personalAbbr == abbr) {
                return shape;
            }
        }

        throw new IllegalArgumentException("Unexpected abbr " + abbr);
    }
}

enum Outcome {
    LOSS(0, 'X'), DRAW(3, 'Y'), WIN(6, 'Z');

    private final int score;
    private final int abbr;

    Outcome(int score, int abbr) {
        this.score = score;
        this.abbr = abbr;
    }

    public int getScore() {
        return score;
    }

    public static Outcome getByAbbr(char abbr) {
        for (Outcome outcome : values()) {
            if (outcome.abbr == abbr) {
                return outcome;
            }
        }

        throw new IllegalArgumentException("Unexpected abbr " + abbr);
    }
}
