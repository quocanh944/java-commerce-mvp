package com.example.midterm.config;

import com.example.midterm.model.User;
import com.example.midterm.repos.UserRepository;
import com.example.midterm.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfiguration {

    private final UserService userService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> {
                    auth.requestMatchers("/", "/shop", "/category", "/brand", "/search", "/error").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/cart").authenticated();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login((loginConfigurer) -> loginConfigurer.successHandler(new SavedRequestAwareAuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        System.out.println("---------------Request---------------");
                        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                        System.out.println(oAuth2User.getAttributes().get("email"));

                        if (userService.checkUserExistsByEmail(oAuth2User.getAttributes().get("email").toString())) {
                            super.onAuthenticationSuccess(request, response, authentication);
                            return;
                        } else {
                            User user = new User();
                            user.setEmail(oAuth2User.getAttributes().get("email").toString());
                            user.setName(oAuth2User.getAttributes().get("name").toString());
                            userRepository.save(user);
                        }

                        super.onAuthenticationSuccess(request, response, authentication);
                    }
                }))
                .logout((logout) -> logout.addLogoutHandler(
                        new HeaderWriterLogoutHandler(
                                new ClearSiteDataHeaderWriter(
                                        ClearSiteDataHeaderWriter.Directive.CACHE,
                                        ClearSiteDataHeaderWriter.Directive.COOKIES,
                                        ClearSiteDataHeaderWriter.Directive.STORAGE
                                )
                        )
                ))
                .build();
    }
}
