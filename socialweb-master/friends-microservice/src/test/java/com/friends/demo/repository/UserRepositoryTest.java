package com.friends.demo.repository;

import com.friends.demo.database.BuildingPostgresqlContainer;

import com.friends.demo.model.Friends;
import com.friends.demo.model.Users;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@Testcontainers
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @ClassRule
    @Container
    public static PostgreSQLContainer<BuildingPostgresqlContainer> postgreSQLContainer = BuildingPostgresqlContainer.getInstance();

    @Autowired
    UserRepository userRepository;

    @Before
    public void setUp() {

        Users users = Users.builder()
                .userHistory(new ArrayList<>())
                .userId("1")
                .rating(123)
                .identification_id("21321321321")
                .friends(new Friends())
                .build();

        Users users1 = Users.builder()
                .userHistory(new ArrayList<>())
                .userId("2")
                .rating(321)
                .identification_id("asdwq21wq")
                .friends(new Friends())
                .build();

        userRepository.save(users);
        userRepository.save(users1);
    }

    @After
    public void clearDataBase() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void saveUser_returnUserNotNull() {

        Users users = Users.builder()
                .userHistory(new ArrayList<>())
                .userId("3")
                .rating(555)
                .identification_id("2wedsax2")
                .friends(new Friends())
                .build();
        Users saveUsers = userRepository.save(users);

        Assertions.assertNotNull(saveUsers);
        org.assertj.core.api.Assertions.assertThat(saveUsers.getId()).isGreaterThan(0);
    }

    @Test
    @Transactional
    public void findById_returnUserNotNUll() {
        Users users = Users.builder()
                .userHistory(new ArrayList<>())
                .userId("23")
                .rating(222)
                .identification_id("2wedsax2")
                .friends(new Friends())
                .build();
        Users saveUser = userRepository.save(users);
        Users findUsers = userRepository.findById(saveUser.getId()).get();

        org.assertj.core.api.Assertions.assertThat(findUsers).isNotNull();
    }

    @Test
    @Transactional
    public void findAll_returnUsersList() {

        List<Users> usersList = userRepository.findAll();

        Assertions.assertNotNull(usersList);
        Assertions.assertEquals(usersList.size(), 2);
    }

    @Test
    @Transactional
    public void updateUsersRating_returnUsersNotNull() {
        Users users = Users.builder()
                .userHistory(new ArrayList<>())
                .userId("23")
                .rating(222)
                .identification_id("2wedsax2")
                .friends(new Friends())
                .build();
        Users saveUsers = userRepository.save(users);
        Users findUsers = userRepository.findById(saveUsers.getId()).get();
        users.setRating(0);
        userRepository.save(findUsers);

        Assertions.assertNotNull(findUsers);
        Assertions.assertEquals(findUsers.getRating(), 0);
    }

    @Test
    @Transactional
    public void findByUserId_returnUserNotNull() {

        Users users = userRepository.findByUserId("1").get();

        Assertions.assertNotNull(users);
        Assertions.assertEquals(users.getRating(), 123);
    }

    @Test
    @Transactional
    public void deleteById_returnStatusIsEmpty() {

        Users users = Users.builder()
                .userHistory(new ArrayList<>())
                .userId("5")
                .rating(555)
                .identification_id("2wedsax2")
                .friends(new Friends())
                .build();
        Users saveUser = userRepository.save(users);
        userRepository.deleteById(saveUser.getId());
        Optional<Users> findUsers = userRepository.findById(saveUser.getId());

        org.assertj.core.api.Assertions.assertThat(findUsers).isEmpty();
    }
}
