package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;





// LOGIN: admin  PASSWORD: admin (ДЛЯ АДМИНА)
// LOGIN: user  PASSWORD: user (ДЛЯ ЮЗЕРА)



@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping()
    public String UserInfo(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =  (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }
}
