package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import ru.mephi.tsis.bootlegamazon.exceptions.RoleNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Role;
import ru.mephi.tsis.bootlegamazon.models.User;
import ru.mephi.tsis.bootlegamazon.services.RoleService;
import ru.mephi.tsis.bootlegamazon.services.implementations.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/profile")
public class CustomerProfileController {

    private final UserRepository userRepository;

    private final UserService userService;

    private final UserAuthRepository userAuthRepository;

    private final RoleRepository roleRepository;

    private final RoleService roleService;

    @Autowired
    public CustomerProfileController(UserRepository userRepository, UserService userService, UserAuthRepository userAuthRepository, RoleRepository roleRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userAuthRepository = userAuthRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
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
        model.addAttribute("user", currentUser);
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

    @GetMapping("/all")
    public String all(
            Model model,
            @RequestParam("page") Integer pageNumber,
            @AuthenticationPrincipal UserDetails user
    ){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        Pageable pageable = PageRequest.of(pageNumber, 6, Sort.Direction.ASC, "id");

        int totalPages = userRepository.findAll(pageable).getTotalPages();
        int previousPage = 0;
        int nextPage = 0;
        int currentPage = pageNumber;
        if ((pageNumber >= totalPages) || (pageNumber < 0)){
            return "redirect:/items/all?page=0";
        }
        if (pageNumber == 0){
            previousPage = 0;
            if (totalPages == 1){
                nextPage = 0;
            } else {
                nextPage = currentPage + 1;
            }
        } else if (pageNumber == totalPages-1){
            nextPage = totalPages-1;
            previousPage = currentPage - 1;
        } else {
            nextPage = currentPage + 1;
            previousPage = currentPage - 1;
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("previousPage", previousPage);

        ArrayList<User> users = new ArrayList<>();
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        for(UserEntity userEntity : userEntities) {
            users.add(new User(userEntity.getId(), userEntity.getLogin(), userEntity.getName(), roleRepository.findById(userEntity.getRoleId()).get().getName()));
        }

        model.addAttribute("users", users);

        return "profiles-page";
    }

    @GetMapping("/adminchange")
    public String editRole(
            Model model,
            @RequestParam("userid") Integer userId,
            @AuthenticationPrincipal UserDetails currentUser
    ){

        String userRole = userAuthRepository.findByUsername(currentUser.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        UserEntity userEntity = userRepository.findById(userId).get();
        User user = null;
        try {
            user = new User(userEntity.getId(), userEntity.getLogin(), userEntity.getName(), roleService.getById(userEntity.getRoleId()).getRoleName());
        } catch (RoleNotFoundException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("user", user);
        List<Role> roles = roleService.getAll(((o1, o2) -> o1.getName().compareTo(o2.getName())));
        model.addAttribute("roles", roles);
        return "profile-admin-edit-page";
    }

    @PostMapping("/savechangedrole")
    public String saveChangedRole(
            Model model,
            @ModelAttribute("user") User user,
            RedirectAttributes attributes,
            @AuthenticationPrincipal UserDetails currentUser
    ){
        String userRole = userAuthRepository.findByUsername(currentUser.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        UserEntity userEntity = userRepository.findById(user.getId()).get();
        try {
            userEntity.setRoleId(roleService.getByName(user.getRole(), (o1, o2) -> o1.getName().compareTo(o2.getName())).getRoleId());
            userRepository.save(userEntity);
        } catch (RoleNotFoundException e) {
            throw new RuntimeException(e);
        }
        attributes.addAttribute("userid", user.getId());
        return "redirect:/profile/adminchange";
    }

    @PostMapping("/delete")
    public String deleteUser(
            Model model,
            @ModelAttribute("user") User user,
            RedirectAttributes attributes,
            @AuthenticationPrincipal UserDetails currentUser
    ){
        String userRole = userAuthRepository.findByUsername(currentUser.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        UserEntity userEntity = userRepository.findById(user.getId()).get();
        userEntity.setActive(false);
        userRepository.save(userEntity);
        return "redirect:/profile/all?page=0";
    }

}
