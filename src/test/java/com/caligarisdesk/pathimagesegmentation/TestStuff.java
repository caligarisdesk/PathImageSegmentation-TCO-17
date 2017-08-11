package com.caligarisdesk.pathimagesegmentation;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


public class TestStuff {

    @Test
    public void testFill() {
        int[][] input = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };
        int height = 12;
        int width = 16;
        Filler filler = new Filler(input, 30);
        int score = filler.fill(0, 1, 0);
        System.out.println("score: " + score);
        assertThat(score).isEqualTo(31);
        assertThat(input[3][3]).isEqualTo(0);

    }

    @Test
    public void testFill2() {
        int[][] input = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };
        int height = 12;
        int width = 16;
        Filler filler = new Filler(input, 300);
        int score = filler.fill(0, 1, 0);
        System.out.println("score: " + score);
        assertThat(score).isEqualTo(188);
        assertThat(input[3][3]).isEqualTo(0);

    }

    @Test
    public void testFillLine() {
        int[][] input = {
                {1, 0, 1, 1}
        };
        int height = 4;
        int width = 1;
        Filler filler = new Filler(input, 30);
        int score = filler.fill(0, 2, 0);
        System.out.println("score: " + score);
        assertThat(score).isEqualTo(2);
        assertThat(input[0][3]).isEqualTo(0);

    }

    @Test @Ignore
    public void testAlreadyCovered() {
        int[][] input1= createAll1s(10,10);
        int[][] input2 = Filler.deepCopy(input1);

        Filler filler1 = new Filler(input1, 5);
        int score1 = filler1.fill(0,0,0);
        filler1.setTempMatrix(input2);
        int score2 = filler1.fill(1,0,0);
        System.out.println("score1: " + score1);
        System.out.println("score2: " + score2);
        assertThat(score1).isGreaterThan(score2);

    }

    private int[][] createAll1s(int width, int height) {
        int[][] input = new int[100][100];
        IntStream.range(0, 100 * 100).parallel().forEach(iter -> {
            int x = iter % 100;
            int y = iter / 100;
            input[x][y] = 1;
        });
        return input;
    }



    @Test
    public void testFillFlow() {
        int height = 5;
        int width = 5;

        int[][] input1= createAll1s(width,height);
        input1[2][2] = 0;
        input1[2][3] = 0;
        input1[3][2] = 0;
        input1[3][3] = 0;

        Filler fill = new Filler(input1, 2);
        for (int i = 0; i < width * height; i++) {
            int x = i % width;
            int y = i / width;
            int target = 1;

            if (fill.validatePixel(x, y, target)) {
                int[][] outMatrixFilledTemp = Filler.deepCopy(input1);
                fill.setTempMatrix(outMatrixFilledTemp);
                int score = fill.fill(x, y, target);
                System.out.println("score: "+score);
                if (score < 3)
                    input1 = outMatrixFilledTemp;
            }
        }

        for (int x = 0; x < width; x++) {
            String line = "";
            for (int y = 0; y < height; y++) {
                line += input1[x][y];
            }
            System.out.println(line + "\n");

        }
    }

}
