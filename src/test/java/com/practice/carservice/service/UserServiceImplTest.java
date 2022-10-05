package com.practice.carservice.service;

import com.practice.carservice.domain.Role;
import com.practice.carservice.domain.User;
import com.practice.carservice.repo.RoleRepo;
import com.practice.carservice.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Disabled
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private RoleRepo roleRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl underTest;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepo, roleRepo, passwordEncoder);
        user = new User("Test", "Test", "test@test.test",
                "test", "test", "1234567", new ArrayList<>());
        role = new Role("ROLE_ADMIN");
    }

    @Test
    void canLoadUserByUsername() {
        given(userRepo.findByUsername(anyString()))
                .willReturn(Optional.of(user));

        underTest.loadUserByUsername(anyString());

        verify(userRepo).findByUsername(anyString());
    }

    @Test
    void itWillThrowWhenUsernameDoesNotExist() {
        assertThatThrownBy(() -> underTest.loadUserByUsername(user.getUsername()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User " + user.getUsername() + " not found");
    }

    @Test
    void canGetUserById() {
        given(userRepo.findById(anyLong()))
                .willReturn(Optional.of(user));

        underTest.getUserById(anyLong());

        verify(userRepo).findById(anyLong());
    }

    @Test
    void canGetUserByUsername() {
        given(userRepo.findByUsername(anyString()))
                .willReturn(Optional.of(user));

        underTest.getUserByUsername(anyString());

        verify(userRepo).findByUsername(anyString());
    }

    @Test
    void isUserRegistered() {
        underTest.isUserRegistered(anyString());

        verify(userRepo).existsByUsername(anyString());
    }

    @Test
    void canGetUsers() {
        underTest.getUsers();

        verify(userRepo).findAll();
    }

    @Test
    void itShouldSaveUser() {
        underTest.saveUser(user);

        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void itShouldSaveRole() {
        underTest.saveRole(role);

        ArgumentCaptor<Role> roleArgumentCaptor =
                ArgumentCaptor.forClass(Role.class);
        verify(roleRepo).save(roleArgumentCaptor.capture());

        Role capturedRole = roleArgumentCaptor.getValue();
        assertThat(capturedRole).isEqualTo(role);
    }

    @Test
    void itShouldAddRoleToUser() {
        given(userRepo.findByUsername(anyString()))
                .willReturn(Optional.of(user));
        given(roleRepo.findByName(anyString()))
                .willReturn(Optional.of(role));

        underTest.addRoleToUser(user.getUsername(), role.getName());

        verify(userRepo).findByUsername(user.getUsername());
        verify(roleRepo).findByName(role.getName());
    }
}