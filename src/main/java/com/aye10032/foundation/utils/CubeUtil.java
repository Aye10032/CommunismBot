package com.aye10032.foundation.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @program: communismbot
 * @className: CubeUtil
 * @Description: 打乱图案生成
 * @version: v1.0
 * @author: Aye10032
 * @date: 2023/5/31 下午 8:19
 */
public class CubeUtil {
    public static final int YELLOW = 1;
    public static final int WHITE = 2;
    public static final int BLUE = 3;
    public static final int GREEN = 4;
    public static final int ORANGE = 5;
    public static final int RED = 6;

    public static final int FRONT = 0;
    public static final int BACK = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;
    public static final int LEFT = 4;
    public static final int RIGHT = 5;
    public static final Map<Character, Integer> LAYER_MAP = new HashMap<>();

    static {
        LAYER_MAP.put('U', UP);
        LAYER_MAP.put('L', LEFT);
        LAYER_MAP.put('F', FRONT);
        LAYER_MAP.put('B', BACK);
        LAYER_MAP.put('R', RIGHT);
        LAYER_MAP.put('D', DOWN);
    }

    public static final Map<Integer, Color> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put(YELLOW, Color.YELLOW);
        COLOR_MAP.put(WHITE, Color.WHITE);
        COLOR_MAP.put(BLUE, Color.BLUE);
        COLOR_MAP.put(GREEN, Color.GREEN);
        COLOR_MAP.put(ORANGE, Color.ORANGE);
        COLOR_MAP.put(RED, Color.RED);
    }

    public static int[][][] initCube() {
        int[][][] cube = new int[6][3][3];
        for (int i = 0; i < 3; i++) {
            Arrays.fill(cube[UP][i], WHITE);
            Arrays.fill(cube[DOWN][i], YELLOW);
            Arrays.fill(cube[FRONT][i], GREEN);
            Arrays.fill(cube[BACK][i], BLUE);
            Arrays.fill(cube[LEFT][i], ORANGE);
            Arrays.fill(cube[RIGHT][i], RED);
        }
        return cube;
    }

    public static void scrambleCube(String formula, int[][][] cube) {
        String[] steps = formula.split(" ");
        for (String step : steps) {
            boolean clockwise = true;
            int layer;
            int length = step.length();
            char move = step.charAt(0);
            if (!LAYER_MAP.containsKey(move)) {
                continue;
            }
            layer = LAYER_MAP.get(move);
            if (length == 2) {
                clockwise = !(step.charAt(1) == '\'');
            }
            if (step.endsWith("2")) {
                IntStream.range(0, 2).forEach(i -> rotateLayer(cube, layer, true));
            } else {
                rotateLayer(cube, layer, clockwise);
            }
        }
    }

    public static void rotateLayer(int[][][] cube, int layer, boolean clockwise) {
        int[] temp;
        switch (layer) {
            case UP:
                cube[UP] = rotate(cube[UP], clockwise);

                temp = cube[FRONT][0].clone();
                if (clockwise) {
                    cube[FRONT][0] = cube[RIGHT][0].clone();
                    cube[RIGHT][0] = cube[BACK][0].clone();
                    cube[BACK][0] = cube[LEFT][0].clone();
                    cube[LEFT][0] = temp;
                } else {
                    cube[FRONT][0] = cube[LEFT][0].clone();
                    cube[LEFT][0] = cube[BACK][0].clone();
                    cube[BACK][0] = cube[RIGHT][0].clone();
                    cube[RIGHT][0] = temp;
                }
                break;
            case FRONT:
                cube[FRONT] = rotate(cube[FRONT], clockwise);

                temp = cube[UP][2].clone();

                for (int i = 0; i < 3; i++) {
                    if (clockwise) {
                        cube[UP][2][i] = cube[LEFT][2 - i][2];
                        cube[LEFT][2 - i][2] = cube[DOWN][0][2 - i];
                        cube[DOWN][0][2 - i] = cube[RIGHT][i][0];
                        cube[RIGHT][i][0] = temp[i];
                    } else {
                        cube[UP][2][i] = cube[RIGHT][i][0];
                        cube[RIGHT][i][0] = cube[DOWN][0][2 - i];
                        cube[DOWN][0][2 - i] = cube[LEFT][2 - i][2];
                        cube[LEFT][2 - i][2] = temp[i];
                    }
                }
                break;
            case RIGHT:
                cube[RIGHT] = rotate(cube[RIGHT], clockwise);

                temp = IntStream.range(0, 3)
                        .map(i -> cube[UP][i][2])
                        .toArray();
                for (int i = 0; i < 3; i++) {
                    if (clockwise) {
                        cube[UP][i][2] = cube[FRONT][i][2];
                        cube[FRONT][i][2] = cube[DOWN][i][2];
                        cube[DOWN][i][2] = cube[BACK][2 - i][0];
                        cube[BACK][2 - i][0] = temp[i];
                    } else {
                        cube[UP][i][2] = cube[BACK][2 - i][0];
                        cube[BACK][2 - i][0] = cube[DOWN][i][2];
                        cube[DOWN][i][2] = cube[FRONT][i][2];
                        cube[FRONT][i][2] = temp[i];
                    }
                }
                break;
            case LEFT:
                cube[LEFT] = rotate(cube[LEFT], clockwise);

                temp = IntStream.range(0, 3)
                        .map(i -> cube[UP][i][0])
                        .toArray();
                for (int i = 0; i < 3; i++) {
                    if (clockwise) {
                        cube[UP][i][0] = cube[BACK][2 - i][2];
                        cube[BACK][2 - i][2] = cube[DOWN][i][0];
                        cube[DOWN][i][0] = cube[FRONT][i][0];
                        cube[FRONT][i][0] = temp[i];
                    } else {
                        cube[UP][i][0] = cube[FRONT][i][0];
                        cube[FRONT][i][0] = cube[DOWN][i][0];
                        cube[DOWN][i][0] = cube[BACK][2 - i][2];
                        cube[BACK][2 - i][2] = temp[i];
                    }
                }
                break;
            case BACK:
                cube[BACK] = rotate(cube[BACK], clockwise);

                temp = cube[UP][0].clone();

                for (int i = 0; i < 3; i++) {
                    if (clockwise) {
                        cube[UP][0][2 - i] = cube[RIGHT][2 - i][2];
                        cube[RIGHT][2 - i][2] = cube[DOWN][2][i];
                        cube[DOWN][2][i] = cube[LEFT][i][0];
                        cube[LEFT][i][0] = temp[2 - i];
                    } else {
                        cube[UP][0][2 - i] = cube[LEFT][i][0];
                        cube[LEFT][i][0] = cube[DOWN][2][i];
                        cube[DOWN][2][i] = cube[RIGHT][2 - i][2];
                        cube[RIGHT][2 - i][2] = temp[2 - i];
                    }
                }
                break;
            case DOWN:
                cube[DOWN] = rotate(cube[DOWN], clockwise);

                temp = cube[FRONT][2].clone();
                if (clockwise) {
                    cube[FRONT][2] = cube[LEFT][2].clone();
                    cube[LEFT][2] = cube[BACK][2].clone();
                    cube[BACK][2] = cube[RIGHT][2].clone();
                    cube[RIGHT][2] = temp;
                } else {
                    cube[FRONT][2] = cube[RIGHT][2].clone();
                    cube[RIGHT][2] = cube[BACK][2].clone();
                    cube[BACK][2] = cube[LEFT][2].clone();
                    cube[LEFT][2] = temp;
                }
                break;
        }
    }

    public static int[][] rotate(int[][] matrix, boolean clockwise) {
        int[][] newMatrix = new int[3][3];

        if (clockwise) {
            //顺时针
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    newMatrix[j][2 - i] = matrix[i][j];
                }
            }
        } else {
            //逆时针
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    newMatrix[i][j] = matrix[j][2 - i];
                }
            }
        }

        return newMatrix;
    }


    public static void drawImage(int[][][] cube, String path) {
        try {
            int width = 280;
            int height = 220;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    g2d.setColor(COLOR_MAP.get(cube[UP][i][j]));
                    g2d.fillRect(140 + 20 * j, 20 + 20 * i, 20, 20);

                    g2d.setColor(COLOR_MAP.get(cube[BACK][i][j]));
                    g2d.fillRect(20 + 20 * j, 80 + 20 * i, 20, 20);

                    g2d.setColor(COLOR_MAP.get(cube[LEFT][i][j]));
                    g2d.fillRect(80 + 20 * j, 80 + 20 * i, 20, 20);

                    g2d.setColor(COLOR_MAP.get(cube[FRONT][i][j]));
                    g2d.fillRect(140 + 20 * j, 80 + 20 * i, 20, 20);

                    g2d.setColor(COLOR_MAP.get(cube[RIGHT][i][j]));
                    g2d.fillRect(200 + 20 * j, 80 + 20 * i, 20, 20);

                    g2d.setColor(COLOR_MAP.get(cube[DOWN][i][j]));
                    g2d.fillRect(140 + 20 * j, 140 + 20 * i, 20, 20);
                }
            }

            g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            g2d.setColor(Color.GRAY);
            for (int i = 1; i <= 13; i++) {
                g2d.drawLine(20 * i, 80, 20 * i, 140);
            }
            for (int i = 1; i <= 10; i++) {
                g2d.drawLine(140, 20 * i, 200, 20 * i);
            }
            g2d.drawLine(20, 100, 260, 100);
            g2d.drawLine(20, 120, 260, 120);
            g2d.drawLine(160, 20, 160, 200);
            g2d.drawLine(180, 20, 180, 200);

            g2d.setColor(Color.BLACK);
            g2d.drawLine(140, 20, 200, 20);
            g2d.drawLine(140, 200, 200, 200);
            g2d.drawLine(20, 80, 260, 80);
            g2d.drawLine(20, 140, 260, 140);
            g2d.drawLine(20, 80, 20, 140);
            g2d.drawLine(80, 80, 80, 140);
            g2d.drawLine(140, 20, 140, 200);
            g2d.drawLine(200, 20, 200, 200);
            g2d.drawLine(260, 80, 260, 140);

            File output = new File(path);
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
