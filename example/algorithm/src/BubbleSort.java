public class BubbleSort {
    public static void main(String[] args) {

    }

    public static void bubbleSort(int[] array){

        int n = array.length;
        int temp;
        for (int i = 0; i < n; i++){
            for (int j = 1; j < n - 1; j++){
                if (array[j - 1] > array[j]) {
                    temp = array[j - 1];
                    array[j-1] = array[j];
                    array[j] = temp;
                }
            }
        }
    }
}
