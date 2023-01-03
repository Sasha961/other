public class QuickSort {
    public static void main(String[] args) {

    }

    public static void quickSort(int[] array, int from, int to){
        if (from < to){
            int pivot = partition(array, from , to); // перекидывает элементы и ищет центральный
            quickSort(array, from, pivot - 1);
            quickSort(array, pivot + 1, to);
        }
    }
}
