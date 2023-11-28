package ru.dverkask.springapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dverkask.springapp.config.WebSecurityConfig;
import ru.dverkask.springapp.domain.Role;
import ru.dverkask.springapp.domain.entity.UserEntity;
import ru.dverkask.springapp.repositories.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@AllArgsConstructor
public class HomeController {
    private final UserRepository userRepository;
    @GetMapping("/")
    public String home(Model model) {
        UserEntity userEntity = WebSecurityConfig.getUserEntity(userRepository);
        model.addAttribute("currentUser", userEntity.getUsername());

        Set<Role> roles = userEntity.getRoles();
        if (roles.contains(Role.ADMIN)) {
            return "admin/admin";
        } else if (roles.contains(Role.SELLER)) {
            return "seller/seller";
        } else if (roles.contains(Role.STOREKEEPER)) {
            return "storekeeper/storekeeper";
        } else {
            return "redirect:/access-denied";
        }
    }
}
