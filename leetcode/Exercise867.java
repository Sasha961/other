
public class Exercise867 {
    public static void main(String[] args) {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        transposeMatrix(matrix);
    }

    public static int[][] transposeMatrix(int[][] matrix) {
        int [][]transposeMatrix = new int[matrix[0].length][matrix.length];
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[i].length; j++) {
                transposeMatrix[j][i] = matrix[i][j];
            }
        }
        return transposeMatrix;
    }
}

