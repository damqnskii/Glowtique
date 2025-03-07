package com.glowtique.glowtique.web;

import com.glowtique.glowtique.cart.model.Cart;
import com.glowtique.glowtique.cart.service.CartService;
import com.glowtique.glowtique.exception.UnauthorizedException;
import com.glowtique.glowtique.security.AuthenticationMetadata;
import com.glowtique.glowtique.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.glowtique.glowtique.user.model.User;

import java.util.UUID;

@Controller
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }
    @GetMapping("/cart")
    public ModelAndView getCartPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("cart");
        modelAndView.addObject("user", userService.getUserById(authenticationMetadata.getUserId()));

        Cart cart = cartService.getCartByUser(authenticationMetadata.getUserId());
        modelAndView.addObject("cart", cart);

        return modelAndView;
    }

    @PostMapping("/cart/add/{productId}")
    public String addItemToCart(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                              @PathVariable UUID productId,
                                              @RequestParam int quantity) {
        if (authenticationMetadata == null || authenticationMetadata.getUserId() == null) {
            return "redirect:/login";
        }
        UUID userId = authenticationMetadata.getUserId();
        Cart updatedCart = cartService.addItemToCart(userId, productId, quantity);
        return "redirect:/cart";
    }
}
