public class Exercise566 {
    public static void main(String[] args) {
        int[][] matrix = {{1, 2}, {3, 4}};
        int r = 1, c = 4;
        matrixReshape(matrix, r, c);
    }

    public static int[][] matrixReshape(int[][] matrix, int r, int c) {
        int[][] matrixReshape = new int[r][c];
        if (matrix[0].length * matrix.length != r * c || (r == matrix[0].length && c == matrix.length)) {
            return matrix;
        }
        for (int i = 0; i < matrix.length * matrix[0].length; i++) {
            matrixReshape[i / c][i % c] = matrix[i / matrix[0].length][i % matrix[0].length];
        }
        return matrixReshape;
    }
}

