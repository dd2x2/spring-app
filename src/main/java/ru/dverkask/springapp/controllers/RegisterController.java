package ru.dverkask.springapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.dverkask.springapp.domain.Role;
import ru.dverkask.springapp.domain.UserEntity;
import ru.dverkask.springapp.repositories.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@AllArgsConstructor
public class RegisterController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserEntity());
        model.addAttribute("allRoles", Arrays.asList(Role.values()));

        return "register";
    }

    @PostMapping("/process_register")
    public String processRegister(UserEntity user,
                                  HttpServletRequest request) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        String[] requestRoles = request.getParameterValues("roles");
        if (requestRoles != null) {
            Set<Role> roles = Arrays.stream(requestRoles)
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        userRepository.save(user);

        return "redirect:/";
    }
}
