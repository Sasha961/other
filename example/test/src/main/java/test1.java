import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class test1 {

    private static final String REGEX = "[a-z, A-Z]+";
    private static final String REGEX_NUMBER = "[0-9]+";

    public static void main(String[] args) throws ParseException, IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String N = reader.readLine();

        if (numberIsCorrect(N)) {

            String result = "";

            for (int i = 0; i < Integer.parseInt(N); i++) {

                String array = reader.readLine();

                String[] fullNameAndDate = array.split(",");

                if (fullNameAndDate.length == 6) {

                    if (fullNameAndDate[3].matches(REGEX_NUMBER) && fullNameAndDate[4].matches(REGEX_NUMBER) && fullNameAndDate[5].matches(REGEX_NUMBER)) {

                        if (arrayIsCorrect(fullNameAndDate)) {

                            int sumNumberBirthday = 0;
                            String sum = fullNameAndDate[3] + fullNameAndDate[4];

                            for (int j = 0; j < sum.length(); j++) {
                                sumNumberBirthday += Integer.parseInt(String.valueOf(sum.charAt(j)));
                            }

                            String fullName = fullNameAndDate[0] + fullNameAndDate[1] + fullNameAndDate[2];

                            List<Character> check = new ArrayList<>();

                            for (int k = 0; k < fullName.length(); k++) {

                                if (!check.contains(fullName.charAt(k))) {
                                    check.add(fullName.charAt(k));
                                }
                            }

                            int fullNameLength = check.size();
                            String name = fullNameAndDate[0].toUpperCase();

                            int index = (int) name.charAt(0) - (int) 'A' + 1;

                            int cipher = fullNameLength + sumNumberBirthday * 64 + index * 256;

                            String convert = Integer.toHexString(cipher).substring(1).toUpperCase();

                            result += convert + " ";
                        }
                    }
                }
            }
            System.out.println(result);
        }
        reader.close();
    }

    private static boolean arrayIsCorrect(String[] array1) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy");
        Date date = format.parse(array1[3] + "." + array1[4] + "." + array1[5]);

        if ((Integer.parseInt(array1[3]) < 1 || Integer.parseInt(array1[3]) > 31)){
            return false;
        }
        if (Integer.parseInt(array1[4]) < 1 || Integer.parseInt(array1[4]) > 12){
            return false;
        }
        if (Integer.parseInt(array1[5]) < 1950 || Integer.parseInt(array1[5]) > 2021){
            return false;
        }

        Date dateAfter = format.parse("30.12.2021");
        Date dateBefore = format.parse("1.1.1950");

        for (int i = 0; i < 3; i++) {
            if ((array1[i].length() < 1 || array1[i].length() > 15)
                    && !array1[i].matches(REGEX)) {
                return false;
            }
        }

        if (date.after(dateAfter)) {
            return false;
        }

        if (date.before(dateBefore)){
            return false;
        }

        return true;
    }

    public static boolean numberIsCorrect(String number) {

        if (!number.matches(REGEX_NUMBER)){
            return false;
        }

        if (Integer.parseInt(number) >= 1 && Integer.parseInt(number) <= 10_000) {
            return true;
        }
        return false;
    }
}