package com.glowtique.glowtique.web;

import com.glowtique.glowtique.category.model.Category;
import com.glowtique.glowtique.category.model.CategoryType;
import com.glowtique.glowtique.product.model.Product;
import com.glowtique.glowtique.product.service.ProductService;
import com.glowtique.glowtique.security.AuthenticationMetadata;
import com.glowtique.glowtique.user.model.User;
import com.glowtique.glowtique.user.service.UserService;
import com.glowtique.glowtique.web.mapper.ProductWishlistMapper;
import com.glowtique.glowtique.wishlistitem.model.WishlistItem;
import com.glowtique.glowtique.wishlistitem.service.WishlistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class ProductController {
    private final ProductService productService;
    private final UserService userService;
    private final WishlistItemService wishlistItemService;


    @Autowired
    public ProductController(ProductService productService, UserService userService, WishlistItemService wishlistItemService) {
        this.productService = productService;
        this.userService = userService;
        this.wishlistItemService = wishlistItemService;
    }

    @GetMapping("/products")
    public ModelAndView getProductPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, Model model,
                                       @RequestParam(value = "category", required = false) CategoryType category) {
        User user = userService.getUserById(authenticationMetadata.getUserId());
        ModelAndView modelAndView = new ModelAndView();
        List<Product> products = (category != null) ? productService.findByCategory(category) : productService.findAll();

        List<WishlistItem> wishListed = wishlistItemService.wishListedItems(user);
        List<UUID> wishListIds = wishListed.stream().map(w -> w.getProduct().getId()).toList();

        modelAndView.setViewName("products");
        modelAndView.addObject("user", user);
        modelAndView.addObject("products", products);
        modelAndView.addObject("wishListed", wishListIds);
        modelAndView.addObject("wishlistItems", wishListed);

        return modelAndView;
    }

    @GetMapping("/product/{id}")
    public ModelAndView getProductDetails(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                          @PathVariable UUID id) {
        User user = userService.getUserById(authenticationMetadata.getUserId());
        Product product = productService.getProductById(id);
        List<Product> productsWithSameName = productService.getProductsByName(product.getName());

        List<WishlistItem> wishListed = wishlistItemService.wishListedItems(user);
        List<UUID> wishListIds = wishListed.stream().map(w -> w.getProduct().getId()).toList();


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("product-details");
        modelAndView.addObject("product", product);
        modelAndView.addObject("relatedProducts", productsWithSameName);
        modelAndView.addObject("user", user);
        modelAndView.addObject("wishListed", wishListIds);

        return modelAndView;
    }
}
