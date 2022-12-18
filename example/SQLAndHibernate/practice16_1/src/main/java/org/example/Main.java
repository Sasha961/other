package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/skillbox";
        String user = "root";
        String pass = "608370";

        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT concat(course_name,\" - \", round(count(course_name)/" +
                            "((max(month(subscription_date))-min(month(subscription_date))) + 1), 2)) as " +
                            "nameAndCount\n" +
                            "FROM purchaseList  \n" +
                            "group BY course_name ;\n");
            while (resultSet.next()) {
                String nameAndCount = resultSet.getString("nameAndCount");
                System.out.println(nameAndCount);
            }
            connection.close();
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}