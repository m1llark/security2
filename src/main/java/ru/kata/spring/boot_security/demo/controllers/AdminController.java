package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import ru.kata.spring.boot_security.demo.util.MyValidator;

import javax.validation.Valid;


@Controller
public class AdminController {

    private final RoleServiceImpl roleServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final MyValidator myValidator;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl, PasswordEncoder passwordEncoder, MyValidator myValidator) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.myValidator = myValidator;
    }
    //admin admin
    //user user


    @GetMapping(value= {"/"})
    public ModelAndView getAllUsers() {
        ModelAndView model = new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =  (User) authentication.getPrincipal();
        model.addObject("user", user);
        model.addObject("listUsers", userServiceImpl.listUsers());
        model.addObject("listRoles", roleServiceImpl.listRoles());
        model.setViewName("users");
        return model;
    }


    @PutMapping("/update/{id}")
    public String updateUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("listRoles", roleServiceImpl.listRoles());
        if (userServiceImpl.loadUserByUsername(user.getUsername()).getPassword().equals(user.getPassword())) {
            userServiceImpl.updateUser(user);
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userServiceImpl.updateUser(user);
        }
        return "redirect:/";
    }


    @GetMapping(value= {"/add"})
    public ModelAndView addUser() {
        ModelAndView model = new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =  (User) authentication.getPrincipal();
        model.addObject("user", user);
        model.addObject("listRoles", roleServiceImpl.listRoles());
        model.setViewName("adduser");
        return model;
    }


    @PostMapping("/save")
    public String confirmationAddUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("listRoles", roleServiceImpl.listRoles());
        myValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "adduser";

        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userServiceImpl.saveUser(user);
            return "redirect:/";
        }
    }


//    @PostMapping("/addNew")
//    public String addNew(User user) {
//        userServiceImpl.saveUser(user);
//        return "redirect:/";
//    }



    @DeleteMapping(value="/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") Long id) {
        userServiceImpl.removeUserById(id);
        return new ModelAndView("redirect:/");
    }



}
