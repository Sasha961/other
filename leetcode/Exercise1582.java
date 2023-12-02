public class Exercise1582 {
    public static void main(String[] args) {
        int[][] matrix = {{0, 1, 0}, {0, 0, 0}, {1, 0, 0}, {1, 0, 0}};
        System.out.println(numSpecial(matrix));
    }

    public static int numSpecial(int[][] matrix) {
        int count = 0;
        for (int i = 0; i < matrix.length; i++) {
            int numbers = 0;
            int position = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    position = j;
                    numbers++;
                    for (int k = 0; k < matrix.length; k++) {
                        if (matrix[k][position] == 1) {
                            numbers++;
                        }
                    }
                }
            }
            if (numbers == 2) {
                count++;
            }
        }
        return count;
    }
}

