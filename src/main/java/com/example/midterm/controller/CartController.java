package com.example.midterm.controller;

import com.example.midterm.dto.AddToCartDTO;
import com.example.midterm.model.User;
import com.example.midterm.services.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;

    @GetMapping()
    public String cart(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) throws Exception {
        User user = userService.getUserByEmail(oAuth2User.getAttribute("email").toString());

        model.addAttribute("cartItems", shoppingCartService.getCartItemsByUserId(user));
        model.addAttribute("totalPrice", shoppingCartService.getTotalPrice(user));
        model.addAttribute("user", user);

        return "cart";
    }

    @PostMapping()
    public String addToCart(
            @RequestParam String productId,
            @RequestParam String quantity,
            Model model,
            @AuthenticationPrincipal OAuth2User oAuth2User
    ) throws Exception {
        AddToCartDTO addToCartDTO = new AddToCartDTO();
        addToCartDTO.setProductId(Long.valueOf(productId));
        addToCartDTO.setQuantity(Long.valueOf(quantity));

        User user = userService.getUserByEmail(oAuth2User.getAttribute("email").toString());
        shoppingCartService.addProductToCart(addToCartDTO, user);

        return "redirect:/cart";
    }

    @PostMapping("/delete")
    public String deleteProduct(
            @RequestParam String productId,
            Model model,
            @AuthenticationPrincipal OAuth2User oAuth2User) throws Exception {
        shoppingCartService.removeProductFromCart(productId, oAuth2User.getAttribute("email").toString());

        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateProduct(
            @RequestParam String productId,
            @RequestParam String quantity,
            Model model,
            @AuthenticationPrincipal OAuth2User oAuth2User
    ) throws Exception {
        AddToCartDTO addToCartDTO = new AddToCartDTO();
        addToCartDTO.setProductId(Long.valueOf(productId));
        addToCartDTO.setQuantity(Long.valueOf(quantity));

        User user = userService.getUserByEmail(oAuth2User.getAttribute("email").toString());
        shoppingCartService.updateProductToCart(addToCartDTO, user);

        return "redirect:/cart";
    }
}
