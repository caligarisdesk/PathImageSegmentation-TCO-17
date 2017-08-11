package com.caligarisdesk.pathimagesegmentation;

import ij.ImagePlus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Main {

    private static long start;

    public static void main(String[] args) {
        start = System.nanoTime();
/*
        for (int threshold = 10000; threshold < 14001; threshold += 1000) {
            for (int guess = -1; guess < 3; guess++) {
                doIt(args[0], threshold, guess);
                System.out.println("Elapsed: " + (System.nanoTime() - start) / (1000000000) + "s");
            }
        }
        */
        for (int bb = 7; bb <= 15; bb+=3) {
            for(int ft = 200; ft < 5000; ft += 100) {
                doIt(args[0], bb, ft);
            }
        }
    }

    private static void doIt(String inputFolder, int blurBox, int fillThresh) {
        int threshold = 11000;
        List<Double> scores = new ArrayList<>();

        File[] inputFiles = (new File(inputFolder)).listFiles();
        List<File> inputFilesList = Arrays.asList(inputFiles);
        inputFilesList.stream().parallel().forEach(f -> {

            if (f.getName().contains("DS_Store")) {
                return;
            }
            //read image file
            ImagePlus img = new ImagePlus(f.getAbsolutePath());
            Path output = Paths.get("results/" + f.getName().replace(".tif", "_mask.txt"));

            List<String> lines = new ArrayList<>();
            int height = img.getHeight();
            int width = img.getWidth();

            Integer[][] outMatrix = new Integer[width][height];

            int scale = -1 * 5;
            int red = 87 + scale;
            int green = 59 + scale;
            int blue = 116 + scale;

            IntStream.range(0, width * height).parallel().forEach(iter -> {
                int x = iter % width;
                int y = iter / width;

                int[] c = img.getPixel(x, y);
                int score = (c[0] - red) * (c[0] - red) + (c[1] - green) * (c[1] - green) + (c[2] - blue) * (c[2] - blue);
                if (score < threshold)
                    outMatrix[x][y] = 1;
                else
                    outMatrix[x][y] = 0;
            });

            int[][] outMatrixBlur = new int[width][height];
            IntStream.range(0, width * height).forEach(iter -> {
                int x = iter % width;
                int y = iter / width;
                int totalPix = 0;
                int sum = 0;
                for (int n = Math.max(x - blurBox, 0); n <= Math.min(width - 1, x + blurBox); n++) {
                    for (int m = Math.max(y - blurBox, 0); m <= Math.min(height - 1, y + blurBox); m++) {
                        totalPix++;
                        sum += outMatrix[n][m];
                    }
                }
                double score = (double) sum / (double) totalPix;
                if (score > 0.3)
                    outMatrixBlur[x][y] = 1;
                else
                    outMatrixBlur[x][y] = 0;
            });

            int[][] outMatrixFilled = Filler.deepCopy(outMatrixBlur);


            Filler fill = new Filler(outMatrixFilled, fillThresh + 5);
            for (int i = 0; i < width * height; i++) {
                int x = i % width;
                int y = i / width;
                int target = 1;

                if (fill.validatePixel(x, y, target)) {
                    int[][] outMatrixFilledTemp = Filler.deepCopy(outMatrixFilled);
                    fill.setTempMatrix(outMatrixFilledTemp);
                    int score = fill.fill(x, y, target);

                    if (score < fillThresh)
                        outMatrixFilled = outMatrixFilledTemp;
                }
            }

            for (int x = 0; x < width; x++) {
                String line = "";
                for (int y = 0; y < height; y++) {
                    line += outMatrixFilled[x][y];
                }
                lines.add(line);
            }

            try {
                Files.write(output, lines);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            File truth = new File(f.getAbsolutePath()
                    .replace("images", "truth")
                    .replace(".tif", "_mask.txt"));
            if (truth.exists()) {
                scores.add(ScoreCalculator.calcScore(output.toFile(), truth));
            }
        });
        double totalScore = scores.stream().mapToDouble(a -> a).average().getAsDouble();
        System.out.println("Total score: " + totalScore + ", blur box: " + blurBox + ", ft: " + fillThresh);
        System.out.println("Elapsed: " + (System.nanoTime() - start) / (1000000000) + "s");
    }
}
