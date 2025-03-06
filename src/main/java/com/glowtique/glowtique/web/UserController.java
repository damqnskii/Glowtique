package com.glowtique.glowtique.web;

import com.glowtique.glowtique.security.AuthenticationMetadata;
import com.glowtique.glowtique.user.model.CustomUserDetailsService;
import com.glowtique.glowtique.user.service.UserService;
import com.glowtique.glowtique.web.dto.EditProfileRequest;
import com.glowtique.glowtique.web.dto.ProductRequest;
import com.glowtique.glowtique.web.mapper.DtoMapper;
import com.glowtique.glowtique.wishlistitem.model.WishlistItem;
import com.glowtique.glowtique.wishlistitem.service.WishlistItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.glowtique.glowtique.user.model.User;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping
public class UserController {
    private final UserService userService;
    private final WishlistItemService wishlistItemService;

    @Autowired
    public UserController(UserService userService, WishlistItemService wishlistItemService) {
        this.userService = userService;
        this.wishlistItemService = wishlistItemService;
    }


    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ModelAndView getAllUsers(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        List<User> users = userService.getAllUsers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        modelAndView.addObject("users", users);

        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/admin-dashboard")
    public ModelAndView getAdminDashboard() {
        return new ModelAndView("admin-dashboard");
    }

    @GetMapping("/profile")
    public ModelAndView getProfileMenu(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getUserById(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/edit-profile")
    public ModelAndView getEditProfileForm(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getUserById(authenticationMetadata.getUserId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("editProfileRequest", DtoMapper.mapUserToEditRequest(user));
        return modelAndView;
    }

    @PutMapping("/edit-profile")
    public ModelAndView updateUser(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, @Valid EditProfileRequest editProfileRequest, BindingResult bindingResult) {
        UUID userId = authenticationMetadata.getUserId();
        User user = userService.getUserById(authenticationMetadata.getUserId());

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("editProfileRequest", editProfileRequest);
            return modelAndView;
        }
        userService.updateUser(userId, editProfileRequest);

        return new ModelAndView("redirect:/profile");

    }
    @GetMapping("/wishlist")
    public ModelAndView getWishlist(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getUserById(authenticationMetadata.getUserId());
        List<ProductRequest> wishlistItems = wishlistItemService.getWishlistItems(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("wishlist");
        modelAndView.addObject("wishlistItems", wishlistItems);
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
