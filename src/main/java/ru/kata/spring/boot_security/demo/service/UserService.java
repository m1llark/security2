package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    public List<User> listUsers();

    public boolean removeUserById(Long userId);

    public void saveUser(User user);


    public void updateUser(User user);

}
