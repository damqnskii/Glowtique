package com.glowtique.glowtique.web;

import com.glowtique.glowtique.brand.model.Brand;
import com.glowtique.glowtique.brand.repository.BrandRepository;
import com.glowtique.glowtique.brand.service.BrandService;
import com.glowtique.glowtique.category.model.Category;
import com.glowtique.glowtique.category.model.CategoryType;
import com.glowtique.glowtique.product.model.Product;
import com.glowtique.glowtique.product.service.ProductService;
import com.glowtique.glowtique.security.AuthenticationMetadata;
import com.glowtique.glowtique.user.model.User;
import com.glowtique.glowtique.user.service.UserService;
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
    private final BrandService brandService;

    @Autowired
    public ProductController(ProductService productService, UserService userService, WishlistItemService wishlistItemService, BrandService brandService) {
        this.productService = productService;
        this.userService = userService;
        this.wishlistItemService = wishlistItemService;
        this.brandService = brandService;
    }

    @GetMapping("/products")
    public ModelAndView getProductPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, Model model,
                                       @RequestParam(value = "category", required = false) CategoryType category) {
        if (authenticationMetadata == null) {
            return new ModelAndView("redirect:/login");
        }
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
        if (authenticationMetadata == null) {
            return new ModelAndView("redirect:/login");
        }

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

    @GetMapping("/product-brand/{brandId}")
    public ModelAndView getBrandProducts(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                            @PathVariable UUID brandId) {
        if (authenticationMetadata == null) {
            return new ModelAndView("redirect:/login");
        }
        List<Product> products = productService.getAllProductsByBrandId(brandId);
        List<WishlistItem> wishlistItems = wishlistItemService.wishListedItems(userService.getUserById(authenticationMetadata.getUserId()));
        List<UUID> wishListedIds = wishlistItems.stream().map(w -> w.getProduct().getId()).toList();
        Brand brand = brandService.getBrandById(brandId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("product-brand");
        modelAndView.addObject("products", products);
        modelAndView.addObject("wishListed", wishListedIds);
        modelAndView.addObject("wishlistItems", wishlistItems);
        modelAndView.addObject("brand", brand);

        return modelAndView;
    }
}
