import java.util.Arrays;

public class Exercise1337 {
    public static void main(String[] args) {
        int[][] matrix = {
                {1, 1, 0, 0, 0},
                {1, 1, 1, 1, 0},
                {1, 0, 0, 0, 0},
                {1, 1, 0, 0, 0},
                {1, 1, 1, 1, 1}
        };
        //2 0 3
        int k = 3;
        System.out.println(Arrays.toString(kWeakestRows(matrix, k)));
    }

    public static int[] kWeakestRows(int[][] mat, int k) {
        int[] soldersPosition = new int[mat.length];
        int count = 0;
        for (int[] ints : mat) {
            int solders = 0;
            for (int anInt : ints) {
                if (anInt == 1) {
                    solders++;
                }
            }
            soldersPosition[count] = solders;
            count++;
        }
        int[] result = new int[k];
        int countResult = 0;
        int position = 0;
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < soldersPosition.length; j++) {
                if (soldersPosition[j] < soldersPosition[position]) {
                    position = j;
                }
            }
            result[countResult] = position;
            soldersPosition[result[countResult]] = Integer.MAX_VALUE;
            countResult++;
            position = 0;
        }
        return result;
    }
}

