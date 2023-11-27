import java.util.ArrayList;
import java.util.List;

public class Exercise1380 {
    public static void main(String[] args) {
        int[][] matrix = {
                {1, 10, 4, 2},
                {9, 3, 8, 7},
                {15, 16, 17, 12}};
        // минимум в строке макс в столбце
        System.out.println(luckyNumbers(matrix));
    }


    public static List<Integer> luckyNumbers(int[][] matrix) {
        List<Integer> luckyNumbers = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            int number = matrix[i][0];
            int position = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] < number) {
                    number = matrix[i][j];
                    position = j;
                }
            }
            for (int k = 0; k < matrix.length; k++) {
                if (matrix[k][position] > number) {
                    number = 0;
                }
            }
            if (number != 0) {
                luckyNumbers.add(number);
            }
        }
        return luckyNumbers;
    }
}

