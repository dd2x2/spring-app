package ru.dverkask.springapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.dverkask.springapp.config.WebSecurityConfig;
import ru.dverkask.springapp.domain.entity.Administrator;
import ru.dverkask.springapp.domain.Goods;
import ru.dverkask.springapp.domain.Role;
import ru.dverkask.springapp.domain.entity.UserEntity;
import ru.dverkask.springapp.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
public class AdminController {
    private final Administrator administrator;
    private final UserRepository userRepository;
    @GetMapping("/admin/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserEntity());
        model.addAttribute("allRoles", Arrays.asList(Role.values()));
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());

        return "admin/register";
    }

    @PostMapping("/admin/process_register")
    public String processRegister(UserEntity user,
                                  HttpServletRequest request,
                                  Model model) {
        administrator.registerUser(user, request);
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());

        return "redirect:/login";
    }

    @GetMapping("/admin/goods")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllGoods(Model model) {
        List<Goods> goods = administrator.getAllProducts();
        model.addAttribute("goods", goods);
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());

        return "admin/goods";
    }

    @GetMapping("/admin/goods/add")
    public String addGoods(Model model) {
        model.addAttribute("goods", new Goods());
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());
        return "admin/add_goods";
    }

    @PostMapping("/admin/goods/add")
    public String saveGoods(@ModelAttribute("goods") Goods goods,
                            Model model) {
        administrator.createGoods(goods);
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());

        return "redirect:/admin/goods";
    }

    @GetMapping("/admin/goods/delete/{id}")
    public String deleteGoods(@PathVariable("id") Long id,
                              Model model) {
        administrator.deleteProduct(id);
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());

        return "redirect:/admin/goods";
    }

    @PostMapping("/admin/goods/edit/{id}")
    public String editGoods(@PathVariable("id") Long id,
                            @ModelAttribute("goods") Goods goods,
                            Model model) {
        administrator.updateGoods(id, goods);
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());

        return "redirect:/admin/goods";
    }

    @GetMapping("/admin/goods/edit/{id}")
    public String showEditGoods(Model model,
                                @PathVariable("id") Long id) {
        Goods goods = administrator.getGoods(id);
        model.addAttribute("goods", goods);
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());
        
        return "admin/edit_goods";
    }
}
