package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserAuth;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.RoleRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserRepository;
import ru.mephi.tsis.bootlegamazon.models.CustomerProfile;
import ru.mephi.tsis.bootlegamazon.services.implementations.UserService;

import javax.validation.Valid;


@Controller
@RequestMapping("/profile")
public class CustomerProfileController {

    private UserRepository userRepository;

    private UserService userService;

    private UserAuthRepository userAuthRepository;

    private RoleRepository roleRepository;

    @Autowired
    public CustomerProfileController(UserRepository userRepository, UserService userService, UserAuthRepository userAuthRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userAuthRepository = userAuthRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("")
    public String customerProfile(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("currentUser", user);
        return "profile";
    }

    @GetMapping("/{id}")
    public String anyProfile (@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        UserAuth loggedUser = userAuthRepository.findByUsername(currentUser.getUsername());
        UserEntity user = userRepository.findById(id).get();
        String role = roleRepository.findById(user.getRoleId()).get().getName();
        if (!loggedUser.getId().equals(user.getId())) {
            return "error-page";
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("role", role);
        return "profile";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        UserAuth loggedUser = userAuthRepository.findByUsername(currentUser.getUsername());
        UserEntity user = userRepository.findById(id).get();
        if (!loggedUser.getId().equals(user.getId())) {
            return "error-page";
        }
        model.addAttribute("currentUser", user);
        return "profile-edit-page";
    }

    @PostMapping("/saveedit")
    public String saveEdit(@ModelAttribute("user") @Valid UserEntity user, Model model, @AuthenticationPrincipal UserDetails currentUser, RedirectAttributes redirectAttributes) {
        UserAuth loggedUser = userAuthRepository.findByUsername(currentUser.getUsername());
        int id = loggedUser.getId();

        if (user.getName().equals("") || user.getPassword().equals("") || user.getPasswordConfirm().equals("")) {
            redirectAttributes.addFlashAttribute("fieldError", "Все поля должны быть заполнены!");
            return "redirect:/profile/" + id + "/edit";
        }

        if (!user.getPassword().equals(user.getPasswordConfirm())){
            redirectAttributes.addFlashAttribute("passwordError", "Пароли не совпадают");
            return "redirect:/profile/" + id + "/edit";
        }
        userService.update(id, user.getName(), user.getPassword());
        return "redirect:/profile/"+ id;
    }

}
