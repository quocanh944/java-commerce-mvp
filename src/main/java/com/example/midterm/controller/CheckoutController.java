package com.example.midterm.controller;

import com.example.midterm.model.User;
import com.example.midterm.services.ShoppingCartService;
import com.example.midterm.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;

    @GetMapping()
    public String checkout(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) throws Exception {

        User user = userService.getUserByEmail(oAuth2User.getAttribute("email").toString());

        model.addAttribute("cartItems", shoppingCartService.getCartItemsByUserId(user));
        model.addAttribute("totalPrice", shoppingCartService.getTotalPrice(user));
        model.addAttribute("user", user);

        return "checkout";
    }

    @PostMapping
    public String createOrder(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String message,
            Model model,
            @AuthenticationPrincipal OAuth2User oAuth2User
    ) throws Exception {

        User user = userService.getUserByEmail(oAuth2User.getAttribute("email").toString());

        model.addAttribute("user", user);

        String fullAddress = address.concat(", ").concat(city);
        shoppingCartService.checkout(name, email, fullAddress, message, user);

        return "success";
    }

}
