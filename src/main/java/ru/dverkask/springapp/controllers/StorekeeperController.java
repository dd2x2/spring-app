package ru.dverkask.springapp.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dverkask.springapp.config.WebSecurityConfig;
import ru.dverkask.springapp.domain.*;
import ru.dverkask.springapp.repositories.OrderGoodsRepository;
import ru.dverkask.springapp.repositories.OrderRepository;
import ru.dverkask.springapp.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/storekeeper")
@AllArgsConstructor
public class StorekeeperController {
    private final Storekeeper storekeeper;
    private final Administrator administrator;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderGoodsRepository orderGoodsRepository;
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
    public @ResponseBody ResponseEntity<?> completeOrder(@PathVariable Long id,
                                                         @RequestParam("goods") List<Long> goodsIds) {
        try {
            storekeeper.completeOrder(id, goodsIds);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
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

    @GetMapping("/goods/return")
    public String returnGoods(Model model) {
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());
        List<Order> orders = orderRepository.findByStatus(Order.OrderStatus.ACCEPTED);
        Map<Order, List<OrderGoods>> ordersWithReturnedGoods = new HashMap<>();

        for (Order order : orders) {
            List<OrderGoods> goods = orderGoodsRepository.findByOrderAndStatus(
                    order, OrderGoods.GatherStatus.GATHERED);

            if (!goods.isEmpty()) {
                ordersWithReturnedGoods.put(order, goods);
            }
        }

        orders = new ArrayList<>(ordersWithReturnedGoods.keySet());

        model.addAttribute("orders", orders);
        model.addAttribute("ordersWithReturnedGoods", ordersWithReturnedGoods);

        return "storekeeper/return";
    }

    @PostMapping("/goods/return")
    public String returnGoods(@RequestParam("orderId") Long orderId) {
        storekeeper.returnGoods(orderId);

        return "redirect:/storekeeper/goods/return";
    }
}