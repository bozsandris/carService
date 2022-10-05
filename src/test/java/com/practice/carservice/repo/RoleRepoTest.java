package com.practice.carservice.repo;

import com.practice.carservice.domain.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Disabled
class RoleRepoTest {

    @Autowired
    private RoleRepo underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindByName() {
        String roleName = "ROLE_ADMIN";
        Role role = new Role(roleName);
        underTest.save(role);

        assertThat(underTest.findByName(roleName)).isPresent();
    }

    @Test
    void itShouldNotFindByName() {
        String roleName = "ROLE_ADMIN";
        Role role = new Role(roleName);
        underTest.save(role);

        assertThat(underTest.findByName("ROLE_USER")).isNotPresent();
    }
}