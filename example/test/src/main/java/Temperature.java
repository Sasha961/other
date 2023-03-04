import java.util.Scanner;

public class Temperature {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String d = null;

        for (int i = 0; i <= n; i++) {
            d = sc.nextLine();
        }

        String[] lastDay = d.split(" ");

        if (lastDay[1].equals("-") || (lastDay[1].equals("0+")
                && Integer.parseInt(lastDay[0]) < 0)) {
            System.out.println(0);
        } else if (lastDay[1].equals("0+") && Integer.parseInt(lastDay[0]) > 0) {
            System.out.println(lastDay[0]);
        } else {
            System.out.println("inf");
        }
    }
}
