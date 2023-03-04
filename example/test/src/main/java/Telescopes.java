import java.util.Scanner;

public class Telescopes {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int neededTelescopes = 0;

        String[] nmk = scanner.nextLine().split(" ");
        String[] n = scanner.nextLine().split(" ");

        int m = Integer.parseInt(nmk[1]);

        for (int i = 0; i < m; i++) {
            String[] numberOfTelescope = scanner.nextLine().split(" ");
            int number1 = Integer.parseInt(numberOfTelescope[0]);
            int number2 = Integer.parseInt(numberOfTelescope[1]);

            if (!n[number1 - 1].equals(n[number2 - 1])) {
                neededTelescopes++;
            }
        }
        System.out.println(neededTelescopes);
    }
}
