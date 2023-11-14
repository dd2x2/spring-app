package ru.dverkask.springapp.controllers;

import lombok.AllArgsConstructor;
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
import ru.dverkask.springapp.domain.Goods;
import ru.dverkask.springapp.domain.Order;
import ru.dverkask.springapp.domain.Role;
import ru.dverkask.springapp.domain.entity.Administrator;
import ru.dverkask.springapp.domain.entity.Seller;
import ru.dverkask.springapp.domain.entity.UserEntity;
import ru.dverkask.springapp.repositories.GoodsRepository;
import ru.dverkask.springapp.repositories.OrderRepository;
import ru.dverkask.springapp.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class SellerController {
    private final Administrator administrator;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final GoodsRepository goodsRepository;
    private final Seller seller;
    private final List<Goods> goodsList = new ArrayList<>();

    @GetMapping("/seller/goods")
    @PreAuthorize("hasRole('SELLER')")
    public String getAllGoods(Model model) {
        List<Goods> goods = administrator.getAllProducts();
        model.addAttribute("goods", goods);

        return "seller/goods";
    }

    @GetMapping("/seller/order")
    public String createOrder(Model model) {
        model.addAttribute("goodsList", goodsList);
        return "seller/order";
    }

    @PostMapping("/seller/order")
    public String createOrder() {
        UserEntity userEntity = WebSecurityConfig.getUserEntity(userRepository);

        seller.saveOrder(userEntity.getId(), goodsList);

        goodsList.clear();

        return "redirect:/seller/goods";
    }

    @GetMapping("/seller/order/add")
    public String addGoods(Model model) {
        model.addAttribute("goods", new Goods());
        return "seller/add_goods";
    }

    @PostMapping("/seller/order/add")
    public String addGoods(@ModelAttribute("goods") Goods goods) {
        Goods foundGoods = goodsRepository.findByNameAndManufacturer(goods.getName(), goods.getManufacturer());

        if (foundGoods != null) {
            foundGoods.setCount(goods.getCount());
            goodsList.add(foundGoods);
        }

        return "redirect:/seller/order";
    }
}
