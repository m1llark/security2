package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return userRepository.findByUsername(username).get();
    }


    public User loadUserById(Long id) {
//        Optional<User> user = userRepository.findById(userId);
//        return user.orElse(null);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        return user;
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }


//    public void register(User user) {
//        if (user.getUsername().equals("admin")) {
//            user.setRoles(Set.of(new Role("ROLE_ADMIN"),(new Role("ROLE_USER"))));
//        }
//        else {
//            user.setRoles(Collections.singleton(new Role("ROLE_USER")));
//
//        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }
    public void register(User user) {
        if (user.getUsername().equals("admin")) {
            user.setRoles(Set.of(new Role("ROLE_ADMIN"),(new Role("ROLE_USER"))));
        }

        else {
            user.setRoles(Collections.singleton(new Role("ROLE_USER")));

        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean removeUserById(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

}
