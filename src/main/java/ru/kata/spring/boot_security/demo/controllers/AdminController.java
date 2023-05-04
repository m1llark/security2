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
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.MyValidator;

import javax.validation.Valid;


@Controller
public class AdminController {

    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MyValidator myValidator;

    @Autowired
    public AdminController(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder, MyValidator myValidator) {
        this.roleService = roleService;
        this.userService = userService;

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
        model.addObject("listUsers", userService.listUsers());
        model.addObject("listRoles", roleService.listRoles());
        model.setViewName("users");
        return model;
    }


    @PutMapping("/update/{id}")
    public String updateUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("listRoles", roleService.listRoles());
        if (userService.loadUserByUsername(user.getUsername()).getPassword().equals(user.getPassword())) {
            userService.updateUser(user);
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.updateUser(user);
        }
        return "redirect:/";
    }


    @GetMapping(value= {"/add"})
    public ModelAndView addUser() {
        ModelAndView model = new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =  (User) authentication.getPrincipal();
        model.addObject("user", user);
        model.addObject("listRoles", roleService.listRoles());
        model.setViewName("adduser");
        return model;
    }


    @PostMapping("/save")
    public String confirmationAddUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("listRoles", roleService.listRoles());
        myValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "adduser";

        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
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
        userService.removeUserById(id);
        return new ModelAndView("redirect:/");
    }



}
