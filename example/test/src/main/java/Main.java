
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        List<String> container = new ArrayList<>();
        String[] arr = message.split("");

        for (int i = 0; i < arr.length; i++) {
            if (!container.contains(arr[i]) || arr[i].equals("?")) {
                container.add(arr[i]);
            } else if (container.size() == 10) {
                System.out.println("YES");
                return;
            } else {
                i = i - container.size();
                container.clear();
            }
        }
        System.out.println(container.size() == 10 ? "YES" : "NO");
    }
}

class Cipher {
    public String cipher(String message) {

        List<String> container = new ArrayList<>();
        String[] arr = message.split("");

        for (int i = 0; i < arr.length; i++) {
            if (!container.contains(arr[i]) || arr[i].equals("?")) {
                container.add(arr[i]);
            } else if (container.size() == 10) {
                return "YES";
            } else {
                i = i - container.size();
                container.clear();
            }
        }
        System.out.println();
        return container.size() == 10 ? "YES" : "NO";
    }
}
////
////        SAXParserFactory factory = SAXParserFactory.newInstance(); // для включения
////        SAXParser parser = factory.newSAXParser(); // для парсинга
////        XMLHandler handler = new XMLHandler();// сам наш класс
////        parser.parse(new File(file), handler); // парсинг 1 файл 2 класс
////    }
////    }
//
//    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
//        if(list1 != null && list2 != null){
//
//            if(list1.val < list2.val){
//                list1.next = mergeTwoLists(list1.next, list2);
//                return list1;
//            } else {
//                list2.next = mergeTwoLists(list1, list2.next);
//                return list2;
//            }
//        }
//
//        if(list1 == null){
//            return list2;
//        }
//        return list1;
//
//
//    }
//}

