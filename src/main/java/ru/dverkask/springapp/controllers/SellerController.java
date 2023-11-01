package ru.dverkask.springapp.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.dverkask.springapp.domain.Goods;
import ru.dverkask.springapp.domain.Role;
import ru.dverkask.springapp.domain.entity.Administrator;
import ru.dverkask.springapp.domain.entity.Seller;
import ru.dverkask.springapp.domain.entity.UserEntity;
import ru.dverkask.springapp.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class SellerController {
    private final Administrator administrator;
    private final UserRepository userRepository;
    private final Seller seller;

    @GetMapping("/seller/goods")
    @PreAuthorize("hasRole('SELLER')")
    public String getAllGoods(Model model) {
        List<Goods> goods = administrator.getAllProducts();
        model.addAttribute("goods", goods);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<UserEntity> user = userRepository.findByUsername(currentPrincipalName);

        if (user.isPresent()) {
            UserEntity userEntity = user.get();

            if (userEntity.getRoles().contains(Role.SELLER)) {
                seller.saveOrder(userEntity.getId(), goods);
                System.out.println("найс");
            }
        }

        return "seller/goods";
    }
}
