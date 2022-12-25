package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.models.Cart;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;
import ru.mephi.tsis.bootlegamazon.services.CartService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

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
    public String cart(
            Model model,
            @AuthenticationPrincipal UserDetails user,
            HttpServletResponse response,
            @CookieValue(name = "user-id", defaultValue = "DEFAULT-USER-ID") String userId
    ){
        if(userId.equals("DEFAULT-USER-ID")){
            userId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("user-id", userId);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }

        model.addAttribute("user", user);
        System.out.println(userId);
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
            Model model,
            @RequestParam("articleid") Integer articleId,
            @RequestParam("frompage") Optional<Integer> pageNumber,
            @RequestParam("hrefargs") Optional<String> hrefArgs,
            @AuthenticationPrincipal UserDetails user,
            HttpServletResponse response,
            @CookieValue(name = "user-id", defaultValue = "DEFAULT-USER-ID") String userId
    ){
        if(userId.equals("DEFAULT-USER-ID")){
            userId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("user-id", userId);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }

        //Integer userId = userAuthRepository.findByUsername(user.getUsername()).getId();
        Cart cart = null;

        int currentArticleAmountInStock = 0;
        try {
            Article article = articleService.getById(articleId);
            currentArticleAmountInStock = article.getAmount();
        } catch (ArticleNotFoundException | CategoryNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            cart = cartService.getCartByUserId(userId);


            int articleAmountInCart = cart.getItemAmountByArticleId(articleId);
            if (articleAmountInCart == currentArticleAmountInStock){
                model.addAttribute("errorMessage", "Вы уже добавили максимальное количество этого товара");
                return "error-page";
            }

            cartService.addArticleToCart(cart.getId(), articleId);
        } catch (CartNotFoundException e){
            try {
                cart = cartService.createCartForUser(userId);
                cartService.addArticleToCart(cart.getId(), articleId);
            } catch (CartNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }catch (CategoryNotFoundException | ArticleNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (pageNumber.isPresent()){
            if(hrefArgs.isPresent()){
                return "redirect:/items/all?page=" + pageNumber.get() + hrefArgs.get();
            } else {
                return "redirect:/items/all?page=" + pageNumber.get();
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
            @AuthenticationPrincipal UserDetails user,
            RedirectAttributes attributes,
            HttpServletResponse response,
            @CookieValue(name = "user-id", defaultValue = "DEFAULT-USER-ID") String userId
    ){
        if(userId.equals("DEFAULT-USER-ID")){
            userId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("user-id", userId);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }

        model.addAttribute("user", user);
        //Integer userId = userAuthRepository.findByUsername(user.getUsername()).getId();
        try {
            Cart cart = cartService.getCartByUserId(userId);
            if(method.equals("increase")){

                int currentArticleAmountInStock = 0;
                try {
                    Article article = articleService.getById(articleId);
                    currentArticleAmountInStock = article.getAmount();
                } catch (ArticleNotFoundException | CategoryNotFoundException e) {
                    throw new RuntimeException(e);
                }

                int articleAmountInCart = cart.getItemAmountByArticleId(articleId);
                if (articleAmountInCart == currentArticleAmountInStock){
                    attributes.addFlashAttribute("error", "Вы уже добавили максимальное количество этого товара");
                    return "redirect:/cart";
                }

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
    public String deleteArticleFromCart(
            @RequestParam("articleid") Integer articleId,
            Model model,
            @AuthenticationPrincipal UserDetails user,
            HttpServletResponse response,
            @CookieValue(name = "user-id", defaultValue = "DEFAULT-USER-ID") String userId
    ){
        if(userId.equals("DEFAULT-USER-ID")){
            userId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("user-id", userId);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }

        model.addAttribute("user", user);
        //Integer userId = userAuthRepository.findByUsername(user.getUsername()).getId();;
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
