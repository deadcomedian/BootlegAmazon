package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserAuth;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserRepository;
import ru.mephi.tsis.bootlegamazon.services.implementations.UserService;
import org.springframework.security.core.userdetails.User;

@Controller
@RequestMapping("/profile")
public class CustomerProfileController {

    private UserRepository userRepository;

    private UserService userService;

    private UserAuthRepository userAuthRepository;

    @Autowired
    public CustomerProfileController(UserRepository userRepository, UserService userService, UserAuthRepository userAuthRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userAuthRepository = userAuthRepository;
    }

    @GetMapping("")
    public String customerProfile(Model model, @AuthenticationPrincipal UserEntity user) {
        model.addAttribute("currentUser", user);
        return "profile";
    }

    @GetMapping("/{id}")
    public String anyProfile (@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        //UserEntity user = userRepository.findById(id).get();
        //String username = String.valueOf();
        UserAuth loggedUser = userAuthRepository.findByUsername(currentUser.getUsername());
        UserAuth user = userAuthRepository.findById(id).get();
        if (!loggedUser.getId().equals(user.getId())) {
            return "error-page";
        }
        model.addAttribute("currentUser", user);
        return "profile";
    }

}
