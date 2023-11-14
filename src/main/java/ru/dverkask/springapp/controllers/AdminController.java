package ru.dverkask.springapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.dverkask.springapp.domain.entity.Administrator;
import ru.dverkask.springapp.domain.Goods;
import ru.dverkask.springapp.domain.Role;
import ru.dverkask.springapp.domain.entity.UserEntity;

import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
public class AdminController {
    private final Administrator administrator;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserEntity());
        model.addAttribute("allRoles", Arrays.asList(Role.values()));

        return "admin/register";
    }

    @PostMapping("/process_register")
    public String processRegister(UserEntity user,
                                  HttpServletRequest request) {
        administrator.registerUser(user, request);

        return "redirect:/login";
    }

    @GetMapping("/admin/goods")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllGoods(Model model) {
        List<Goods> goods = administrator.getAllProducts();
        model.addAttribute("goods", goods);

        return "admin/goods";
    }

    @GetMapping("/goods/add")
    public String addGoods(Model model) {
        model.addAttribute("goods", new Goods());
        return "admin/add_goods";
    }

    @PostMapping("goods/add")
    public String saveGoods(@ModelAttribute("goods") Goods goods) {
        administrator.createGoods(goods);

        return "redirect:/admin/goods";
    }

    @GetMapping("/goods/delete/{id}")
    public String deleteGoods(@PathVariable("id") Long id) {
        administrator.deleteProduct(id);

        return "redirect:/goods";
    }

    @PostMapping("/goods/edit/{id}")
    public String editGoods(@PathVariable("id") Long id,
                            @ModelAttribute("goods") Goods goods) {
        administrator.updateGoods(id, goods);

        return "redirect:/goods";
    }

    @GetMapping("/goods/edit/{id}")
    public String showEditGoods(Model model, @PathVariable("id") Long id) {
        Goods goods = administrator.getGoods(id);
        model.addAttribute("goods", goods);
        
        return "admin/edit_goods";
    }
}
