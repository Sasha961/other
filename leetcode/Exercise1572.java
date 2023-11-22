public class Exercise1572 {
    public static void main(String[] args) {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        System.out.println(diagonalSum(matrix));
    }

    public static int diagonalSum(int[][] mat) {
        int sum = 0;
        for (int i = 1; i < mat.length; i++) {
            for (int j = 1; j < mat[0].length; j++) {
                    sum += mat[i - 1][j - 1];
            }
        }
        return sum;
    }
}

