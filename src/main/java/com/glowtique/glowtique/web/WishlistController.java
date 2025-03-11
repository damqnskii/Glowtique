package com.glowtique.glowtique.web;

import com.glowtique.glowtique.exception.UnauthorizedException;
import com.glowtique.glowtique.product.model.Product;
import com.glowtique.glowtique.security.AuthenticationMetadata;
import com.glowtique.glowtique.user.repository.UserRepository;
import com.glowtique.glowtique.user.service.UserService;
import com.glowtique.glowtique.web.dto.ProductRequest;
import com.glowtique.glowtique.wishlistitem.model.WishlistItem;
import com.glowtique.glowtique.wishlistitem.service.WishlistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.glowtique.glowtique.user.model.User;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class WishlistController {
    private final WishlistItemService wishlistItemService;
    private final UserService userService;

    @Autowired
    public WishlistController(WishlistItemService wishlistItemService, UserService userService) {
        this.wishlistItemService = wishlistItemService;
        this.userService = userService;
    }
    @GetMapping("/wishlist/items")
    public ResponseEntity<List<ProductRequest>> getWishlistItems(@AuthenticationPrincipal User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<ProductRequest> wishlistItems = wishlistItemService.getWishlistItems(user);
        return ResponseEntity.ok(wishlistItems);
    }


    @PostMapping("/{context}/add/{productId}")
    public ResponseEntity<Map<String, Object>> addToWishlist(@PathVariable String context,
                                                             @PathVariable UUID productId,
                                                             @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getUserById(authenticationMetadata.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Трябва да сте влезли в акаунта си!"));
        }
        boolean added = wishlistItemService.addToWishlist(productId, user);
        Map<String, Object> response = Map.of(
                "added", added,
                "message", added ? "Продуктът е добавен в списъка с желания!" : "Продуктът е премахнат от списъка с желания!"
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{context}/remove/{productId}")
    public ResponseEntity<Map<String, Object>>  removeFromWishlist(@PathVariable String context,
            @PathVariable UUID productId, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getUserById(authenticationMetadata.getUserId());
        wishlistItemService.removeFromWishlist(productId, user);
        Map<String, Object> response = new HashMap<>();
        response.put("added", false);
        response.put("message", "Продуктът е премахнат от любими!");

        return ResponseEntity.ok(response);
    }
}
