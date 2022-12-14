import java.util.*;

public class Bank implements Comparable<Bank> {

    private Map<String, Account> accounts = new TreeMap<>();

    private List<String> blackList = new Vector<>();

    private final Random random = new Random();

    private boolean list;

    private long allMoney;

    public void addAccounts(String accNumber, long money) {
        accounts.put(accNumber, new Account(accNumber, money));
        allMoney += money;
    }

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    private synchronized boolean blackList(String fromAccountNum, String toAccountNum) {
        if (blackList.contains(fromAccountNum) || blackList.contains(toAccountNum)) {
            System.out.println(Thread.currentThread().getName() + " Один или все счета заблокированы, перевод не возможен");
            return true;
        }
        return false;
    }

    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws Exception {

        if (blackList(fromAccountNum, toAccountNum)) {
            return;
        }
        if (amount > 50_000) {
            block(fromAccountNum, toAccountNum, amount);
        }

        synchronized (accounts.get(fromAccountNum).compareTo(accounts.get(toAccountNum)) > 0 ?
                accounts.get(toAccountNum) : accounts.get(fromAccountNum)) {
            accounts.get(fromAccountNum).setMoney(accounts.get(fromAccountNum).getMoney() - amount);
            accounts.get(toAccountNum).setMoney(accounts.get(toAccountNum).getMoney() + amount);
        }
        System.out.println(Thread.currentThread().getName() + " Перевод выполнен");
    }

    private synchronized void block(String fromAccountNum, String toAccountNum, long amount) throws Exception {

        list = isFraud(fromAccountNum, toAccountNum, amount);

        if (list == true) {
            blackList.add(fromAccountNum);
            blackList.add(toAccountNum);
            System.out.println(Thread.currentThread().getName() + " Блокировка счетов");
            Thread.sleep(1000);
        }
    }

    public long getBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }

    public long getSumAllAccounts() {
        return allMoney;
    }

    @Override
    public int compareTo(Bank o) {
        return o.accounts.get(Account.class).compareTo(o.accounts.get(Account.class));
    }
}
