package test;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MagicSquareExerciseCalculator implements ExerciseCalculator {
    public static final String example = "\t1 2 3\n\t4 5 6\n\t7 8 9";
    public static final List<int[]> magicSquares = generateMagicSquares3x3();

    public @Nullable String validateInput(String input) {
        int firstLineEnd = input.indexOf("\n");
        int secondLineEnd = input.indexOf("\n", firstLineEnd + 1);
        if ((firstLineEnd == -1) || (secondLineEnd == -1)) {
            return "Three lines expected\nExample:\n" + getExample();
        }
        return null;
    }

    /**
     * @throws IllegalArgumentException
     */
    public String calculate(String input) {
        int firstLineEnd = input.indexOf("\n");
        int secondLineEnd = input.indexOf("\n", firstLineEnd + 1);
        if ((firstLineEnd == -1) || (secondLineEnd == -1)) {
            throw new IllegalArgumentException("Three lines expected\nExample:\n" + getExample());
        }

        String[][] lines = new String[][]{
            input.substring(0, firstLineEnd).split("\\s+"),
            input.substring(firstLineEnd + 1, secondLineEnd).split("\\s+"),
            input.substring(secondLineEnd + 1).split("\\s+")
        };
        for (String[] line : lines) {
            if (line.length != 3) {
                throw new IllegalArgumentException("Invalid matrix format\nExample:\n" + getExample());
            }
        }

        int[] matrix = new int[9];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                try {
                    matrix[i * 3 + j] = Integer.parseInt(lines[i][j]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid matrix format\nExample:\n" + getExample());
                }
            }
        }

        if (!isSquareNatural(matrix)) {
            return "Magic square is not natural. Use unique numbers from 1 to 9.";
        }

        int minCost = Integer.MAX_VALUE;
        int[] bestMagicSquare = null;
        for (int[] square : magicSquares) {
            int cost = 0;
            for (int i = 0; i < 9; ++i) {
                cost += Math.abs(matrix[i] - square[i]);
            }
            if (cost < minCost) {
                minCost = cost;
                bestMagicSquare = square;
            }
        }
        if (bestMagicSquare != null) {
            return matrixToStr(bestMagicSquare) + "\nCost: " + minCost;
        } else {
            return "Can't convert to magic square";
        }
    }

    private boolean isSquareNatural(int[] m) {
        for (int i = 0; i < 9; ++i) {
            if ((m[i] < 1) || m[i] > 9) {
                return false;
            }
        }
        for (int i = 0; i < 9; ++i) {
            for (int j = i + 1; j < 9; ++j) {
                if (m[j] == m[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    private String matrixToStr(int[] m) {
        return m[0] + " " + m[1] + " " + m[2] + "\n"
            + m[3] + " " + m[4] + " " + m[5] + "\n"
            + m[6] + " " + m[7] + " " + m[8];
    }

    private static List<int[]> generateMagicSquares3x3() {
        ArrayList<int[]> squares = new ArrayList<>(10);
        RefWrap<int[]> matrix = new RefWrap<>(new int[9]);
        searchMagicSquares(squares, matrix, 0);
        return squares;
    }

    private static void searchMagicSquares(ArrayList<int[]> squares, RefWrap<int[]> matrix, int cellId) {
        for (int a = 1; a <= 9; ++a) {
            if (contains(matrix.val, a, 0, cellId)) {
                continue;
            }
            matrix.val[cellId] = a;
            if (cellId == 8) {
                if (isMagicSquare(matrix.val)) {
                    squares.add(matrix.val);
                    matrix.val = Arrays.copyOf(matrix.val, 9);
                }
            } else {
                searchMagicSquares(squares, matrix, cellId + 1);
            }
        }
    }

    private static boolean isMagicSquare(int[] m) {
        int magicNumber = Arrays.stream(m).sum() / 3;
        return (m[0] + m[1] + m[2] == magicNumber)
            && (m[3] + m[4] + m[5] == magicNumber)
            && (m[6] + m[7] + m[8] == magicNumber)
            && (m[0] + m[3] + m[6] == magicNumber)
            && (m[1] + m[4] + m[7] == magicNumber)
            && (m[2] + m[5] + m[8] == magicNumber);
    }

    private static boolean contains(int[] arr, int value, int from, int to) {
        for (int i = from; i < to; ++i) {
            if (value == arr[i]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getExample() {
        return example;
    }

    public static class RefWrap<T> {
        public T val;

        public RefWrap(T value) {
            this.val = value;
        }
    }
}
