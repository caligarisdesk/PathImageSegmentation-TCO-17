package com.caligarisdesk.pathimagesegmentation;

import java.util.stream.IntStream;

public class Filler {

    private int[][] inputMatrix;
    private int[][] alreadyCovered;
    private int maxFill;
    private int currentFill = 0;
    private int height;
    private int width;

    public Filler(int[][] inputMatrix, int maxFill) {
        this.inputMatrix = inputMatrix;
        this.maxFill = maxFill;
        alreadyCovered = deepCopy(inputMatrix);
        this.height = inputMatrix[0].length;
        this.width = inputMatrix.length;
    }

    public boolean validatePixel(int x, int y, int target) {
        return inputMatrix[x][y] != target &&
                alreadyCovered[x][y] != target;
    }

    public int fill(int x, int y, int target) {
        fillRecurse(x,y,target);
        return currentFill;
    }

    public void setTempMatrix(int [][] newInputMatrix) {
        currentFill = 0;
        this.inputMatrix = newInputMatrix;
    }

    private void fillRecurse(int x, int y, int target) {
        if(currentFill > maxFill) return;
        if (x < width && x >= 0 && y < height && y >= 0) {
            if (inputMatrix[x][y] != target
                    && true){//&& alreadyCovered[x][y] != target) {
                inputMatrix[x][y] = target;
                alreadyCovered[x][y] = target;
                currentFill++;
                fillRecurse(x + 1, y, target);
                fillRecurse(x - 1, y, target);
                fillRecurse(x, y + 1, target);
                fillRecurse(x, y - 1, target);
            }
        }
    }

    public static int[][] deepCopy(final int[][] input) {
        int height = input[0].length;
        int width = input.length;
        int[][] output = new int[width][height];

        IntStream.range(0, width * height).forEach(iter -> {
            int i = iter % width;
            int j = iter / width;
            output[i][j] = input[i][j];
        });

        return output;
    }
}
