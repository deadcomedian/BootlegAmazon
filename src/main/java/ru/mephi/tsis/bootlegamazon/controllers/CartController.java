package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.models.Cart;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;
import ru.mephi.tsis.bootlegamazon.services.CartService;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
@SessionAttributes("cart")
public class CartController {

    private final CartService cartService;
    private final UserAuthRepository userAuthRepository;

    private final ArticleService articleService;

    @Autowired
    public CartController(CartService cartService, UserAuthRepository userAuthRepository, ArticleService articleService) {
        this.cartService = cartService;
        this.userAuthRepository = userAuthRepository;
        this.articleService = articleService;
    }

    @GetMapping("")
    public String cart(Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);
        Integer userId = userAuthRepository.findByUsername(user.getUsername()).getId();
        Cart cart = null;
        try {
            cart = cartService.getCartByUserId(userId);
        } catch (CartNotFoundException e) {
            try {
                cart = cartService.createCartForUser(userId);
            } catch (CartNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        } catch (CategoryNotFoundException | ArticleNotFoundException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("cart", cart);
        return "cart-page";
    }

    @GetMapping("/addtocart")
    public String addArticleToCart(
            @RequestParam("articleid") Integer articleId,
            @RequestParam("frompage") Optional<Integer> pageNumber,
            @RequestParam("hrefargs") Optional<String> hrefArgs,
            @AuthenticationPrincipal UserDetails user
    ){

        Integer userId = userAuthRepository.findByUsername(user.getUsername()).getId();
        try {
            Cart cart = cartService.getCartByUserId(userId);
            cartService.addArticleToCart(cart.getId(), articleId);
        } catch (CartNotFoundException | CategoryNotFoundException | ArticleNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (pageNumber.isPresent()){
            if(hrefArgs.isPresent()){
                return "redirect:/items/all?page=" + pageNumber + hrefArgs;
            } else {
                return "redirect:/items/all?page=" + pageNumber;
            }
        } else {
            return "redirect:/items/" + articleId;
        }
    }


    @PostMapping("/changeamount")
    public String changeArticleAmount(
            @RequestParam("articleid") Integer articleId,
            @RequestParam("changeamount") String method, // increase-decrease
            Model model,
            @AuthenticationPrincipal UserDetails user
    ){
        model.addAttribute("user", user);
        Integer userId = userAuthRepository.findByUsername(user.getUsername()).getId();
        try {
            Cart cart = cartService.getCartByUserId(userId);
            if(method.equals("increase")){
                cartService.addArticleToCart(cart.getId(),articleId);
            } else if (method.equals("decrease")){
                cartService.removeArticleFromCart(cart.getId(),articleId);
            }
        } catch (CartNotFoundException | ArticleNotFoundException | CategoryNotFoundException |
                 CartArticleNotFoundException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/cart";
    }

    @PostMapping("/deletearticle")
    public String deleteArticleFromCart(@RequestParam("articleid") Integer articleId, Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);
        Integer userId = userAuthRepository.findByUsername(user.getUsername()).getId();;
        try {
            Cart cart = cartService.getCartByUserId(userId);
            cartService.deleteArticleFromCartCompletely(cart.getId(), articleId);
        } catch (CartNotFoundException | ArticleNotFoundException | CategoryNotFoundException |
                 CartArticleNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/cart";
    }

}
