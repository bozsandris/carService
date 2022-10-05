package com.practice.carservice.service;

import com.practice.carservice.domain.Role;
import com.practice.carservice.domain.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUserById(Long id);
    List<User> getUsers();
}
