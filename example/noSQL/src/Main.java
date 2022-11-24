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
    }
}
