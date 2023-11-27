import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        int[] results = new int[mat.length * mat[0].length];
        int count = 0;
        int solders = 0;
        for (int i = 0; i < mat.length; i++){
            for (int j = 0; j < mat[i].length; j++){
                if (mat[i][j] == 1) {
                    solders++;
                }
            }
            results[count] = solders;
            count++;
            solders = 0;
        }
        

        return null;
    }
}

