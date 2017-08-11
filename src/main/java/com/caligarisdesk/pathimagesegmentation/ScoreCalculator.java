package com.caligarisdesk.pathimagesegmentation;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class ScoreCalculator {

    public static double calcScore(File prediction, File truth) {
        try {
            List<String> predictions = Files.readAllLines(prediction.toPath());
            List<String> truths = Files.readAllLines(truth.toPath());
            double finalScore = 1000000.0 * (calcMicroF1(predictions, truths) + calcDice(predictions, truths)) / 2.0;
            System.out.println("finalScore: " + finalScore + ", File: " + prediction.getName());
            return finalScore;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot calc score");
        }
        return 0;
    }

    private static double calcMicroF1(List<String> prediction, List<String> truth) {

            /*
            True Positive: Your prediction is white (1) and the truth is white (1).
            False Positive: Your prediction is black (0) and the truth is white (1).
            False Negative: Your prediction is white (1) and the truth is black (0).
            True Negative: Your prediction is black (0) and the truth is black (0).
             */

        double truePositive = 0;
        double falsePositive = 0;
        double trueNegative = 0;
        double falseNegative = 0;


        for (int n = 0; n < truth.size(); n++) {
            String predictionLine = prediction.get(n);
            String truthLine = truth.get(n);
            if (predictionLine.length() != truthLine.length()) {
                throw new RuntimeException("bad file");
            }

            for (int m = 0; m < predictionLine.length(); m++) {
                int predictionValue = Character.getNumericValue(predictionLine.charAt(m));
                int truthValue = Character.getNumericValue(truthLine.charAt(m));
                if (truthValue == 0 && predictionValue == 0) {
                    trueNegative++;
                }
                if (truthValue == 0 && predictionValue == 1) {
                    falseNegative++;
                }
                if (truthValue == 1 && predictionValue == 0) {
                    falsePositive++;
                }
                if (truthValue == 1 && predictionValue == 1) {
                    truePositive++;
                }
            }
        }
        double P = truePositive / (truePositive + falsePositive);
        if ((truePositive + falsePositive) == 0) {
            P = 1;
        }
        double R = truePositive / (truePositive + falseNegative);
        if ((truePositive + falseNegative) == 0) {
            R = 1;
        }
        double score = (2 * P * R / (P + R));
        if ((P + R) == 0) return 0;
        //System.out.println("micro-F1: " + score);
        return score;
    }

    private static double calcDice(List<String> prediction, List<String> truth) {
        double X = 0;
        double G = 0;
        double S = 0;

        for (int n = 0; n < truth.size(); n++) {
            String predictionLine = prediction.get(n);
            String truthLine = truth.get(n);
            if (predictionLine.length() != truthLine.length()) {
                throw new RuntimeException("bad file");
            }

            for (int m = 0; m < predictionLine.length(); m++) {
                int predictionValue = Character.getNumericValue(predictionLine.charAt(m));
                int truthValue = Character.getNumericValue(truthLine.charAt(m));
                if (predictionValue == 1) {
                    S++;
                }
                if (truthValue == 1) {
                    G++;
                }
                if (truthValue == 1 && predictionValue == 1) {
                    X++;
                }
            }
        }
        //2 * |X| / (|G| + |S|)
        double score = (2 * Math.abs(X) / (Math.abs(G) + Math.abs(S)));
        if((Math.abs(G) + Math.abs(S)) == 0) {
            return 1;
        }
        //System.out.println("dice: " + score);
        return score;
    }
}
