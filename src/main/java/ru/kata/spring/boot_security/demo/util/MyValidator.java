package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

@Component
public class MyValidator implements Validator {
    private final UserRepository userRepository;

    @Autowired
    public MyValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        User user2 = userRepository.findByUsername(user.getUsername());
        if (user2 == null) {

        } else {
            if (user2.getId().equals(user.getId())) {

            } else {
                errors.rejectValue("username", "", "User exist");
            }
        }
    }
}
