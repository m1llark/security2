package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.MyValidator;

import javax.validation.Valid;
import java.util.*;


@Controller
public class MainController {

    private final RoleRepository roleRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MyValidator myValidator;

    @Autowired
    public MainController(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder, MyValidator myValidator) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.myValidator = myValidator;
    }
    //admin admin
    //user user


    @RequestMapping(value= {"/"}, method=RequestMethod.GET)
    public ModelAndView getAllUsers() {
        ModelAndView model = new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =  (User) authentication.getPrincipal();
        model.addObject("user", user);
        model.addObject("listUsers", userService.listUsers());
        model.addObject("listRoles", roleRepository.findAll());
        model.setViewName("users");
        return model;
    }


    @PutMapping("/update/{id}")
    public String updateUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("listRoles", userService.listRoles());
        if (userService.loadUserByUsername(user.getUsername()).getPassword().equals(user.getPassword())) {
            userService.saveUser(user);
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        }
        return "redirect:/";
    }


    @RequestMapping(value= {"/add"}, method=RequestMethod.GET)
    public ModelAndView addUser() {
        ModelAndView model = new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =  (User) authentication.getPrincipal();
        model.addObject("user", user);
        model.addObject("listRoles", roleRepository.findAll());
        model.setViewName("adduser");
        return model;
    }


    @PostMapping("/save")
    public String confirmationAddUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("listRoles", userService.listRoles());
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


    @PostMapping("/addNew")
    public String addNew(User user) {
        userService.saveUser(user);
        return "redirect:/";
    }



    @RequestMapping(value="/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return new ModelAndView("redirect:/");
    }


    @GetMapping(("/user"))
    public String UserInfo(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =  (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }
}
