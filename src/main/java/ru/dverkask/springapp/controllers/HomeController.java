package ru.dverkask.springapp.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<UserEntity> user = userRepository.findByUsername(currentUsername);
        Set<Role> roles = new HashSet<>();
        if (user.isPresent())
            roles = user.get().getRoles();
        if (roles.contains(Role.ADMIN)) {
            return "admin/admin";
        } else if (roles.contains(Role.SELLER)) {
            return "seller/seller";
        } else if (roles.contains(Role.STOREKEEPER)) {
            return "storekeeper";
        } else {
            return "redirect:/access-denied";
        }
    }
}
