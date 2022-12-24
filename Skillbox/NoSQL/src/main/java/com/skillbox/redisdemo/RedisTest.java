package com.skillbox.redisdemo;

import java.util.Random;

import static java.lang.System.out;

public class RedisTest {

    private static final int SLEEP = 1000;
    private static final int RANDOM_NUMBER1 = 11;
    private static final int RANDOM_NUMBER2 = 21;

    private static void log(String user) {
        String log = String.format("- На главной странице показываем пользователя %s", user);
        out.println(log);
    }

    private static void logBuyPremium(String user) {
        String log = String.format("> Пользователь %s оплатил платную услугу", user);
        out.println(log);
    }

    public static void main(String[] args) throws InterruptedException {

        RedisStorage redis = new RedisStorage();
        redis.init();

        for (;;) {

            int userRandom1 = new Random().nextInt(RANDOM_NUMBER1);
            int userRandom2 = new Random().nextInt(RANDOM_NUMBER1 - userRandom1) + userRandom1;
            int userRandom3 = new Random().nextInt(RANDOM_NUMBER2 - RANDOM_NUMBER1) + RANDOM_NUMBER1 - 1;
            int userRandom4 = new Random().nextInt(RANDOM_NUMBER2 - userRandom3) + userRandom3;

            for (int user = 1; user <= 20; user++) {

                redis.addBuyPremium(userRandom1, String.valueOf(userRandom2));
                redis.addBuyPremium(userRandom3, String.valueOf(userRandom4));
                redis.addUser(user);
            }

            for (String user : redis.getUsers()) {

                if (Integer.valueOf(user) == userRandom2 || Integer.valueOf(user) == userRandom4) {
                    logBuyPremium(user);
                }
                log(user);
            }

            Thread.sleep(SLEEP);
            out.println();
        }
    }
}
