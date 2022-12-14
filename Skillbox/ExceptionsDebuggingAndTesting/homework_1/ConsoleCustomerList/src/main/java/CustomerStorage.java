import java.util.HashMap;
import java.util.Map;

public class CustomerStorage {
    private final Map<String, Customer> storage;
    private static final String CORRECT_PHONE = "(\\+79){1}[0-9]{9}";//"[+79]{1}[0-9]{9}";
    private static final String CORRECT_EMAIL = "(\\w|\\W){1,}[@]{1}[a-z]{1,}[.][a-z]{1,}";

    public CustomerStorage() {

        storage = new HashMap<>();
    }

    public void addCustomer(String data) {
        final int INDEX_NAME = 0;
        final int INDEX_SURNAME = 1;
        final int INDEX_EMAIL = 2;
        final int INDEX_PHONE = 3;

        String[] components = data.split("\\s+");

        if (components.length == 4 && components[INDEX_PHONE].matches(CORRECT_PHONE)
                && components[INDEX_EMAIL].matches(CORRECT_EMAIL)) {
            String name = components[INDEX_NAME] + " " + components[INDEX_SURNAME];
            storage.put(name, new Customer(name, components[INDEX_PHONE], components[INDEX_EMAIL]));
        } else {
            throw new IllegalArgumentException("Wrong format." + "\n" + "Correct format: add Василий Петров " +
                    "vasily.petrov@gmail.com +79215637722");
        }
    }

    public void listCustomers() {
        storage.values().forEach(System.out::println);
    }

    public void removeCustomer(String name) {
        storage.remove(name);
    }

    public Customer getCustomer(String name) {
        return storage.get(name);
    }

    public int getCount() {
        return storage.size();
    }
}