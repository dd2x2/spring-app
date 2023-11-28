package ru.dverkask.springapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.dverkask.springapp.config.WebSecurityConfig;
import ru.dverkask.springapp.domain.Goods;
import ru.dverkask.springapp.domain.Order;
import ru.dverkask.springapp.domain.entity.Administrator;
import ru.dverkask.springapp.domain.entity.Storekeeper;
import ru.dverkask.springapp.domain.entity.UserEntity;
import ru.dverkask.springapp.repositories.OrderRepository;
import ru.dverkask.springapp.repositories.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/storekeeper")
@AllArgsConstructor
public class StorekeeperController {
    private final Storekeeper storekeeper;
    private final Administrator administrator;
    private final UserRepository userRepository;
    @GetMapping("/orders")
    public String getOrderedOrders(Model model) {
        List<Order> orders = storekeeper.getOrderedOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());
        return "storekeeper/orders";
    }

    @PostMapping("/orders/complete/{id}")
    public String completeOrder(@PathVariable Long id,
                                @RequestParam("goods") List<Long> goodsIds) {
        storekeeper.completeOrder(id, goodsIds);
        return "redirect:/storekeeper/orders";
    }
    @PostMapping("/orders/cancel/{id}")
    public String cancelOrder(@PathVariable Long id) {
        storekeeper.cancelOrder(id);
        return "redirect:/storekeeper/orders";
    }

    @GetMapping("/goods")
    public String goods(Model model) {
        List<Goods> goods = administrator.getAllProducts();
        model.addAttribute("goods", goods);
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());

        return "storekeeper/goods";
    }
}
