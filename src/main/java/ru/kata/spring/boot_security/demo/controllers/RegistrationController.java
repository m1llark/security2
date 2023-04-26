package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.MyValidator;

import javax.persistence.Access;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller

public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MyValidator myValidator;

//    @GetMapping(("/user"))
//    public String UserInfo(ModelMap model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user =  (User) authentication.getPrincipal();
//        model.addAttribute("user", user);
//        return "user";
//    }

    @GetMapping("/registration")
    public String showSignUpForm(@ModelAttribute("user") User user) {
        userService.listRoles();
        return "registration";
    }

    @PostMapping("/registration")
    public String processRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult)  {
        myValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/registration";
        }
        if (user.getUsername().equals("admin")) {
            user.addRole(roleRepository.getById(2L));
            user.addRole(roleRepository.getById(1L));
        } else {
            user.addRole(roleRepository.getById(1L));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }
}
