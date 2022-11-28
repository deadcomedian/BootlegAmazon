package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    @GetMapping("/authorize")
    public String authorize(){
        return "authorization-page";
    }
}
