package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Cart;
import ru.mephi.tsis.bootlegamazon.services.CartService;

@Controller
@RequestMapping("/cart")
@SessionAttributes("cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    //здесь будет UserDetails
    @GetMapping("")
    public String cart(Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);
        Integer userId = 2;
        try {
            Cart cart = cartService.getCartByUserId(userId);
            model.addAttribute("cart", cart);
        } catch (CartNotFoundException | ArticleNotFoundException | CategoryNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "cart-page";
    }

    //здесь будет UserDetails
    @PostMapping("/changeamount")
    public String changeArticleAmount(
            @RequestParam("articleid") Integer articleId,
            @RequestParam("changeamount") String method, // increase-decrease
            Model model,
            @AuthenticationPrincipal UserDetails user
    ){
        model.addAttribute("user", user);
        Integer userId = 2;
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

    //здесь будет UserDetails
    @PostMapping("/deletearticle")
    public String deleteArticleFromCart(@RequestParam("articleid") Integer articleId, Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);
        Integer userId = 2;
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
