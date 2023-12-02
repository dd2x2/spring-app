package ru.dverkask.springapp.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dverkask.springapp.config.WebSecurityConfig;
import ru.dverkask.springapp.domain.*;
import ru.dverkask.springapp.domain.Administrator;
import ru.dverkask.springapp.domain.Seller;
import ru.dverkask.springapp.repositories.*;

import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/seller")
public class SellerController {
    private final Administrator administrator;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ShopGoodsRepository shopGoodsRepository;
    private final OrderGoodsRepository orderGoodsRepository;
    private final Seller seller;
    @GetMapping("/goods")
    public String getAllGoods(Model model) {
        List<ShopGoods> goods = seller.getAllProducts();
        model.addAttribute("goods", goods);
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());

        return "seller/goods";
    }

    @GetMapping("/order")
    public String createOrder(Model model) {
        List<Goods> goods = administrator.getAllProducts();
        model.addAttribute("goods", goods);
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());
        return "seller/order";
    }

    @PostMapping("/order")
    public String createOrder(@RequestParam("selectedgoods") List<Integer> goodsIds,
                              @RequestParam Map<String, String> allParams,
                              Model model) {
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());
        seller.createOrder(goodsIds, allParams);

        return "redirect:/seller/goods";
    }

    @GetMapping("/goods/get")
    public String acceptGoods(Model model) {
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());
        List<Order> completedOrders = orderRepository.findByStatus(Order.OrderStatus.COMPLETED);

        List<OrderGoods> gatheredGoods = new ArrayList<>();
        completedOrders.forEach(order -> {
            List<OrderGoods> list = orderGoodsRepository.findByOrderAndStatus(order, OrderGoods.GatherStatus.GATHERED);
            gatheredGoods.addAll(list);
        });
        model.addAttribute("goods", gatheredGoods);
        model.addAttribute("orders", completedOrders);

        return "seller/get";
    }

    @PostMapping("/goods/get")
    public String acceptGoods(@RequestParam(value = "goods", required = false) List<Long> goodsIds,
                              @RequestParam("orderId") Long orderId) {
        seller.acceptGoods(goodsIds, orderId);

        return "redirect:/seller/goods/get";
    }

    @GetMapping("/goods/give")
    public String releaseGoods(Model model) {
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());
        List<ShopGoods> allShopGoods = (List<ShopGoods>) shopGoodsRepository.findAll();
        model.addAttribute("allShopGoods", allShopGoods);

        return "seller/give";
    }

    @PostMapping("/goods/give")
    public String releaseGoods(@RequestParam Map<String, String> allParams) {
        seller.releaseGoods(allParams);

        return "redirect:/seller/goods/give";
    }
}
