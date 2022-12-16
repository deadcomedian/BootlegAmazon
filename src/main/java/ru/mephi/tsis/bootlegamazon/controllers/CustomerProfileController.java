package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserRepository;

@Controller
@RequestMapping("/profile")
public class CustomerProfileController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public String customerProfile(Model model, @AuthenticationPrincipal UserEntity user) {
        model.addAttribute("currentUser", user);
        return "profile";
    }

    @GetMapping("{id}")
    public String anyProfile (@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UserEntity currentUser) {
        UserEntity user = userRepository.findById(id).get();
      //  if (!currentUser.getId().equals(user.getId())) {
       //     return "error/error";
      //  }
        model.addAttribute("currentUser", user);
        return "profile";
    }

}
