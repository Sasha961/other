import java.util.Collections;

public class MergeSort {
    public static void main(String[] args) {

//        Collections.sort();
    }

    public static void mergeSort(int[] array){
        int n = array.length;
        int middle = n/2;
        int[] leftArray = new int[middle];
        int[] rightArray = new int[n - middle];

        for (int i = 0; i < middle; i++){
            leftArray[i] = array[i];
        }
        for (int i = middle; i < n; i++){
            rightArray[i - middle] = array[i];
        }
        mergeSort(leftArray);
        mergeSort(rightArray);
        merge(array, leftArray, rightArray);
    }
}
