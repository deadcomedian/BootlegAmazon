package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserAuthRepository userAuthRepository;
    @Autowired
    public AdminController(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @GetMapping("/panel")
    public String adminPanel(Model model, @AuthenticationPrincipal UserDetails user){
        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (userRole.equals("Администратор")){
            model.addAttribute("isAdmin", true);
            return "admin-panel-page";
        } else if (userRole.equals("Менеджер")){
            model.addAttribute("isAdmin", false);
            return "admin-panel-page";
        } else {
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }
    }
}
