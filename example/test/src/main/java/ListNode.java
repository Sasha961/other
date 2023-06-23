import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ListNode {
    public static void main(String[] args) {
        System.out.println(generate(5));
    }


    public static List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList();
        result.add(new ArrayList<Integer>(1));
        for (int i = 1; i <= numRows; i++) {
            List<Integer> temp = new ArrayList();
            for (int j = 1; j >= 1; j--) {
                temp.add(j - 1 + j);
            }
            result.add(temp);
        }
        return result;
    }
}
