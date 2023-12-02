import java.util.Arrays;

public class Exercise1886 {
    public static void main(String[] args) {
        int[][] matrix = {{0, 0, 0}, {0, 1, 0}, {1, 1, 1}};
        int[][] target = {{1, 1, 1}, {0, 1, 0}, {0, 0, 0}};

        System.out.println(findRotation(matrix, target));
    }

    public static boolean findRotation(int[][] mat, int[][] target) {
        int k = mat.length;
        int[][] newMat = new int[k][k];
        if (checkMatrix(mat, target)) {
            return true;
        }
        for (int l = 0; l < 4; l++) {
            for (int i = 0; i < mat.length; i++) {
                for (int j = 0; j < mat.length; j++) {
                    newMat[j][mat.length - 1 - i] = mat[i][j];
                }
            }
            mat = newMat;
            newMat = new int[k][k];
            if (checkMatrix(mat, target)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkMatrix(int[][] newMat, int[][] target) {
        for (int i = 0; i < newMat.length; i++) {
            for (int j = 0; j < newMat[0].length; j++) {
                if (newMat[i][j] != target[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}

