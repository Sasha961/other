import java.util.Collection;
import java.util.Collections;

public class Binary {
    public static void main(String[] args) {

//        Collections.binarySearch();
    }

    public static int binary(String query, int from, int to){
        int middle = (from + to) / 2;
        int comparison = query.compareTo(list.get(middle));

        if (comparison == 0){
            return middle;
        }
        if (comparison > 0){
            return binary(query, middle, to);
        }
        if (comparison < 0){
            return binary(query, from, middle);
        }
        return 0;
    }
}
