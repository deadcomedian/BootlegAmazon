package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserEntity;
import ru.mephi.tsis.bootlegamazon.services.implementations.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private UserService userService;
    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserEntity());
        return "registration-new";
    }

    @PostMapping("/create")
    public String addUser(
            @ModelAttribute("userForm") @Valid UserEntity userForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (userForm.getName().equals("") || userForm.getPhone().equals("") || userForm.getLogin().equals("") || userForm.getPassword().equals("") || userForm.getPasswordConfirm().equals("")) {
            redirectAttributes.addFlashAttribute("fieldError", "Все поля должны быть заполнены!");
            return "redirect:/registration";
        }

        if (bindingResult.hasErrors()) {
            return "redirect:/registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            redirectAttributes.addFlashAttribute("passwordError", "Пароли не совпадают");
            return "redirect:/registration";
        }
        if (!userService.createUser(userForm.getName(), userForm.getPhone(), userForm.getLogin(), userForm.getPassword())){
            redirectAttributes.addFlashAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "redirect:/registration";
        }
        return "redirect:/items/all?page=0";
    }

}
