public class Exercise1351 {
    public static void main(String[] args) {
        int[][] matrix = {
                {4, 3, 2, -1},
                {3, 2, 1, -1},
                {1, 1, -1, -2},
                {-1, -1, -2, -3}
        };
        System.out.println(countNegatives(matrix));
    }

    public static int countNegatives(int[][] matrix) {
        int count = 0;
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                if (anInt < 0) {
                    count++;
                }
            }
        }
        return count;
    }
}

