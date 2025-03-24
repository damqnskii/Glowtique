package com.glowtique.glowtique.web;

import com.glowtique.glowtique.brand.model.Brand;
import com.glowtique.glowtique.brand.service.BrandService;
import com.glowtique.glowtique.security.AuthenticationMetadata;
import com.glowtique.glowtique.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import com.glowtique.glowtique.user.model.User;

import java.util.List;
import java.util.UUID;

@Controller
public class BrandController {
    private final BrandService brandService;
    private final UserService userService;

    @Autowired
    public BrandController(BrandService brandService, UserService userService) {
        this.brandService = brandService;
        this.userService = userService;
    }

    @GetMapping("/brands")
    public ModelAndView getBrandsPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        if (authenticationMetadata == null) {
            return new ModelAndView("redirect:/login");
        }
        List<Brand> brands = brandService.getAllBrands();

        ModelAndView modelAndView = new ModelAndView("brands");
        modelAndView.addObject("brands", brands);
        return modelAndView;
    }

    @GetMapping("/brand/{id}")
    public ModelAndView getBrandDetails(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                        @PathVariable UUID id) {
        if (authenticationMetadata == null) {
            return new ModelAndView("redirect:/login");
        }
        Brand currentBrand = brandService.getBrandById(id);

        ModelAndView modelAndView = new ModelAndView("brand-details");
        modelAndView.addObject("brand", currentBrand);

        return modelAndView;
    }
}
