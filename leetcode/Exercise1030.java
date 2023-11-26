import java.util.*;

public class Exercise1030 {
    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(allCellsDistOrder(2, 2, 0, 1)));
    }

    public static int[][] allCellsDistOrder(int rows, int cols, int rCenter, int cCenter) {

        Map<Integer, List<int[]>> map = new TreeMap<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int result = Math.abs(rCenter - i) + Math.abs(cCenter - j);
                int[] mat = {i, j};
                if (!map.containsKey(result)) {
                    map.put(result, new ArrayList<>());
                }
                map.get(result).add(mat);
            }
        }

        int[][] result = new int[rows * cols][];
        int count = 0;
        for (int i = 0; i < map.size(); i++) {
            for (int[] values : map.get(i)) {
                result[count] = values;
                count++;
            }
        }
        return result;
    }
}

