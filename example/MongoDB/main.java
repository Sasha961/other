import java.util.Scanner;

public class main {
    public static void main(String[] args) {

        String str1 = new Scanner(System.in).nextLine();
        String str2 = new Scanner(System.in).nextLine();

        int count = 0;
        String[] mass = str2.split("");

        for (String a : mass) {
            if (str2.contains(a)){
                count++;
            }
        }

        System.out.println(count);
    }
}
