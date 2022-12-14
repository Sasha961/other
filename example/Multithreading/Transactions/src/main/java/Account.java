public class Account implements Comparable<Account> {

    private long money;
    private String accNumber;

    public Account(String accNumber, long money) {
        this.money = money;
        this.accNumber = accNumber;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    @Override
    public int compareTo(Account o) {
        return o.getAccNumber().compareTo(o.getAccNumber());
    }
}
