package ru.dverkask.springapp.domain.entity;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dverkask.springapp.domain.Goods;
import ru.dverkask.springapp.domain.Role;
import ru.dverkask.springapp.repositories.GoodsRepository;
import ru.dverkask.springapp.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class Administrator extends UserEntity {
    private final GoodsRepository goodsRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void registerUser(UserEntity user, HttpServletRequest request) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        String[] requestRoles = request.getParameterValues("roles");
        if (requestRoles != null) {
            Set<Role> roles = Arrays.stream(requestRoles)
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    public Goods getGoods(Long id) {
        if (goodsRepository.findById(id).isPresent())
            return goodsRepository.findById(id).get();
        return null;
    }

    public void createGoods(Goods goods) {
        goodsRepository.save(goods);
    }

    public void updateGoods(Long id,
                            Goods goods) {
        goodsRepository.findById(id)
                .map(product -> {
                    product.setName(goods.getName());
                    product.setManufacturer(goods.getManufacturer());
                    product.setDescription(goods.getDescription());
                    product.setPrice(goods.getPrice());
                    product.setCount(goods.getCount());

                    return goodsRepository.save(product);
                })
                .orElseGet(() -> {
                    goods.setId(id);

                    return goodsRepository.save(goods);
                });
    }

    public void deleteProduct(Long id) {
        goodsRepository.deleteById(id);
    }

    public List<Goods> getAllProducts() {
        return (List<Goods>) goodsRepository.findAll();
    }
}
