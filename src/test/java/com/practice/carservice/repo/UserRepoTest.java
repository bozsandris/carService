package com.practice.carservice.repo;

import com.practice.carservice.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Disabled
class UserRepoTest {

    private Long userId;
    @Autowired
    UserRepo underTest;

    @BeforeEach
    void setUp() {
        User user = new User("Test",
                "Test",
                "test@test.test",
                "test",
                "test",
                "1234567",
                new ArrayList<>());
        userId = underTest.save(user).getId();
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindByUsername() {
        assertThat(underTest.findByUsername("test")).isPresent();
    }

    @Test
    void itShouldNotFindByUsername() {
        assertThat(underTest.findByUsername("admin")).isNotPresent();
    }

    @Test
    void canGetUserById() {
        assertThat(underTest.findById(userId)).isPresent();
    }

    @Test
    void canNotGetUserById() {
        assertThat(underTest.findById(2L)).isNotPresent();
    }

    @Test
    void itShouldCheckIfUserExistsByUsername() {
        assertThat(underTest.existsByUsername("test")).isTrue();
    }

    @Test
    void itShouldCheckIfUserDoesNotExistByUsername() {
        assertThat(underTest.existsByUsername("admin")).isFalse();
    }
}