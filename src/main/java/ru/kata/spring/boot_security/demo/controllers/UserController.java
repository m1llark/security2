package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.MyValidator;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;



@Controller
public class UserController {

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


//    @RequestMapping(value= {"/"}, method=RequestMethod.GET)
//    public ModelAndView getAllUsers() {
//        ModelAndView model = new ModelAndView();
//        List<User> listUsers = userService.listUsers();
//
//        model.addObject("listUsers", listUsers);
//        model.setViewName("users");
//        return model;
//    }
    @RequestMapping(value= {"/"}, method=RequestMethod.GET)
    public ModelAndView getAllUsers() {
        ModelAndView model = new ModelAndView();
        List<User> listUsers = userService.listUsers();


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =  (User) authentication.getPrincipal();
        model.addObject("user", user);

        model.addObject("listUsers", listUsers);
        model.addObject("listRoles", roleRepository.findAll());

        model.addObject("Role1", roleRepository.findById(1l).get());
        model.addObject("Role2", roleRepository.findById(2l).get());

        model.setViewName("users");
        return model;
    }


    @RequestMapping(value="/update/{id}", method=RequestMethod.GET)
    public ModelAndView editUser(@PathVariable Long id) {
        List<Role> listRoles = userService.listRoles();
        ModelAndView model = new ModelAndView();
        User user = userRepository.getById(id);
        model.addObject("user", user);
        model.addObject("listRoles", listRoles);
        model.setViewName("user_form");

        return model;
    }


//    @RequestMapping(value="/add", method=RequestMethod.GET)
//    public ModelAndView addUser() {
//        List<Role> listRoles = userService.listRoles();
//        ModelAndView model = new ModelAndView();
//        User user = new User();
//        model.addObject("user", user);
//        model.addObject("listRoles", listRoles);
//        model.setViewName("user_form");
//        return model;
//    }
//
//    @RequestMapping(value="/save", method=RequestMethod.POST)
//    public ModelAndView saveOrUpdate(@ModelAttribute("userForm")@Valid User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//        return new ModelAndView("redirect:/");
//    }

    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String addUser(User user, Model model) {
//        List<Role> listRoles = userService.listRoles();

        model.addAttribute("user", user);
        model.addAttribute("listRoles", userService.listRoles());
        return "user_form";
    }

    @PostMapping("/save")
    public String confirmationAddUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("listRoles", userService.listRoles());
        myValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "user_form";

        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "redirect:/";
        }
    }
    @PostMapping("/addNew")
    public String addNew(User user) {
        userRepository.save(user);
        return "redirect:/";
    }




    @RequestMapping(value="/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return new ModelAndView("redirect:/");
    }
}
