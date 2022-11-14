package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Setter
//@Getter                                     // если нет базы данных
//public class Book {
//    public int id;
//    private String name;
//    private int year;
//}



                                        // если есть база данных

// <dependency>
//<groupId>org.springframework.boot</groupId>
//<artifactId>spring-boot-starter-data-jpa</artifactId>
//<version>2.1.4.RELEASE</version>
//</dependency>
// файл с настройками application.yml
// create означает очищение базы


@Entity
public class Book {
    @Getter
    @Setter


    @Id // обязательный параметр
    @GeneratedValue(strategy = GenerationType.AUTO) // автоматическая генерация id
    public int id;

    private String name;

    private int year;


}
