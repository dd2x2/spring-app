package ru.dverkask.springapp.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dverkask.springapp.config.WebSecurityConfig;
import ru.dverkask.springapp.domain.*;
import ru.dverkask.springapp.domain.entity.Administrator;
import ru.dverkask.springapp.domain.entity.Seller;
import ru.dverkask.springapp.domain.entity.UserEntity;
import ru.dverkask.springapp.repositories.GoodsRepository;
import ru.dverkask.springapp.repositories.OrderGoodsRepository;
import ru.dverkask.springapp.repositories.OrderRepository;
import ru.dverkask.springapp.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class SellerController {
    private final Administrator administrator;
    private final UserRepository userRepository;
    private final GoodsRepository goodsRepository;
    private final OrderRepository orderRepository;
    private final OrderGoodsRepository orderGoodsRepository;
    private final Seller seller;
    @GetMapping("/seller/goods")
    @PreAuthorize("hasRole('SELLER')")
    public String getAllGoods(Model model) {
        List<ShopGoods> goods = seller.getAllProducts();
        model.addAttribute("goods", goods);
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());

        return "seller/goods";
    }

    @GetMapping("/seller/order")
    public String createOrder(Model model) {
        List<Goods> goods = administrator.getAllProducts();
        model.addAttribute("goods", goods);
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());
        return "seller/order";
    }

    @PostMapping("/seller/order")
    public String createOrder(@RequestParam("selectedgoods") List<Integer> goodsIds,
                              @RequestParam Map<String, String> allParams,
                              Model model) {
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());
        Map<Integer, Integer> goodsAndValues = new HashMap<>();

        goodsIds.forEach(id -> {
            String value = allParams.get("text_" + id);
            if (value != null) {
                goodsAndValues.put(id, Integer.parseInt(value));
            }
        });

        UserEntity userEntity = WebSecurityConfig.getUserEntity(userRepository);

        List<Goods> selectedGoods = new ArrayList<>(goodsAndValues.entrySet().stream()
                .map(entry -> {
                    Goods good = goodsRepository.findById(Long.valueOf(entry.getKey())).orElse(null);
                    Goods goods = new Goods();
                    if (good != null) {
                        goods.setId(good.getId());
                        goods.setCount(entry.getValue());
                        goods.setName(good.getName());
                        goods.setManufacturer(good.getManufacturer());
                        goods.setDescription(good.getDescription());
                        goods.setPrice(good.getPrice());
                    }
                    return goods;
                })
                .toList());

        seller.saveOrder(userEntity.getId(), selectedGoods);

        selectedGoods.clear();

        return "redirect:/seller/goods";
    }

    @GetMapping("/seller/goods/get")
    public String getGoods(Model model) {
        model.addAttribute("currentUser", WebSecurityConfig
                .getUserEntity(userRepository)
                .getUsername());
        List<Order> completedOrders = orderRepository.findByStatus(Order.OrderStatus.COMPLETED);

        List<OrderGoods> gatheredGoods = new ArrayList<>();
        completedOrders.forEach(order -> {
            List<OrderGoods> list = orderGoodsRepository.findByOrderAndStatus(order, OrderGoods.GatherStatus.GATHERED);
            gatheredGoods.addAll(list);
        });
        model.addAttribute("goods", gatheredGoods); // добавляем данные на страницу
        model.addAttribute("orders", completedOrders); // добавляем данные на страницу

        return "seller/get";
    }
}
