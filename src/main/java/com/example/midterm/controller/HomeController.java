package com.example.midterm.controller;

import com.example.midterm.model.User;
import com.example.midterm.services.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) throws Exception {
        model.addAttribute("hotProducts", productService.getAllHotProducts());
        model.addAttribute("allBrands", brandService.getAllBrand());
        model.addAttribute("allCategories", categoryService.getAllCategories());
        System.out.println(oAuth2User);
        if (oAuth2User != null) {
            User user = userService.getUserByEmail(oAuth2User.getAttribute("email").toString());
            System.out.println(shoppingCartService.getCartItemsByUserId(user));
            model.addAttribute("cartItems", shoppingCartService.getCartItemsByUserId(user));

            model.addAttribute("user", user);
        }

        return "index";
    }

    @GetMapping("/shop")
    public String shop(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) throws Exception {
        model.addAttribute("allCategories",  categoryService.getAllCategories());
        model.addAttribute("allBrands", brandService.getAllBrand());

        if (oAuth2User != null) {
            User user = userService.getUserByEmail(oAuth2User.getAttribute("email").toString());
            model.addAttribute("cartItems", shoppingCartService.getCartItemsByUserId(user));

            model.addAttribute("user", user);
        }

        return "shop";
    }

    @GetMapping("/brand")
    public String brand(
            @RequestParam(value = "id", required = false) String id,
            Model model,
            @AuthenticationPrincipal OAuth2User oAuth2User
    ) throws Exception {
        model.addAttribute("allCategories",  categoryService.getAllCategories());
        model.addAttribute("allBrands", brandService.getAllBrand());

        if (oAuth2User != null) {
            User user = userService.getUserByEmail(oAuth2User.getAttribute("email").toString());
            model.addAttribute("cartItems", shoppingCartService.getCartItemsByUserId(user));

            model.addAttribute("user", user);
        }

        if (id != null && !id.isEmpty()) {
            model.addAttribute("brand", brandService.getBrandById(Long.valueOf(id)));
            return "brand";
        }

        return "brand";
    }

    @GetMapping("/category")
    public String category(
            @RequestParam(value = "id", required = false) String id,
            Model model,
            @AuthenticationPrincipal OAuth2User oAuth2User
    ) throws Exception {
        model.addAttribute("allCategories",  categoryService.getAllCategories());
        model.addAttribute("allBrands", brandService.getAllBrand());

        System.out.println(oAuth2User);

        if (oAuth2User != null) {
            User user = userService.getUserByEmail(oAuth2User.getAttribute("email").toString());
            model.addAttribute("cartItems", shoppingCartService.getCartItemsByUserId(user));

            model.addAttribute("user", user);
        }

        if (id != null && !id.isEmpty()) {
            model.addAttribute("category", categoryService.getCategoryById(Long.valueOf(id)));
            return "category";
        }

        return "category";
    }
}
