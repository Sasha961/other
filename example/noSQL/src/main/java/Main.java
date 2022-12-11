import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        //redis
        // откоючение docker kill skill-redis
        //redis-server запуск сервера
        //redis-cli запуск приложения
        //в командной строке set имя ключя пробел значение
        // пример SET "Number of house for student Petrenko" 19
        // get чтение ключа имя ключа
        // KEYS * информация о всех ключах *и занчение - фильтр
        // SADD - ключ пробел много значений через пробел в ковычках
        // SADD - так же при добавлении к существующим курсам
        // пример SADD "Courses fo student Petrenko" "Java" "Data analysis"
        //SMEMBERS - все значнеия по ключу
        //пример SMEMBERS "Courses fo student Petrenko"
        //SREM - удаление ключа
        // пример SREM "Courses fo student Petrenko" "Java"
        RedissonClient redisson = Redisson.create();

        RScoredSortedSet<String> set = redisson.getScoredSortedSet("mySortedSet");
        set.add(10, "1");
        set.add(20, "2");
        set.add(30, "3");

        for (String string : set) {
            // iteration through bulk loaded values
        }

        Map<String, Double> newValues = new HashMap<>();
        newValues.put("4", 40D);
        newValues.put("5", 50D);
        newValues.put("6", 60D);
        int newValuesAmount = set.addAll(newValues);

        Double scoreResult = set.addScore("2", 10);
        set.contains("4");
        set.containsAll(Arrays.asList("3", "4", "5"));

        String firstValue = set.first();
        String lastValue = set.last();

        String polledFirst = set.pollFirst();
        String polledLast = set.pollLast();
        // use read method to fetch all objects
        Collection<String> allValues = set.readAll();

        redisson.shutdown();
    }
}
