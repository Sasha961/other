import junit.framework.TestCase;

import java.util.ArrayList;

public class BankTest extends TestCase {
    Bank bank = new Bank();

    long sum;

    @Override
    protected void setUp(){

        bank.addAccounts("8932", 275_000);
        bank.addAccounts("8931", 0);
        bank.addAccounts("8930", 250_000);
        bank.addAccounts("8929", 0);

        System.out.println(bank.getSumAllAccounts());
        System.out.println(bank.getBalance("8932"));
        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            threads.add(
                    new Thread(() -> {
                        try {
                            bank.transfer("8932", "8931", 55_000);
                            bank.transfer("8930", "8929", 50_000);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }));
        }
        threads.forEach(Thread::start);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(bank.getSumAllAccounts());
        System.out.println("8932 - " + bank.getBalance("8932"));
        System.out.println("8930 - " + bank.getBalance("8930"));

        sum = bank.getSumAllAccounts();
    }

    public void testGetSumAllAccounts() {
        long expected = 525_000;
        long actual = sum;
        assertEquals(expected, actual);
    }
}
