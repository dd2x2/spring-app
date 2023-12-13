package ru.dverkask.springapp.domain;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.dverkask.springapp.config.WebSecurityConfig;
import ru.dverkask.springapp.repositories.*;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class Seller extends UserEntity {
    private final OrderRepository orderRepository;
    private final GoodsRepository goodsRepository;
    private final OrderGoodsRepository orderGoodsRepository;
    private final ShopGoodsRepository shopGoodsRepository;
    private final UserRepository userRepository;
    private final CheckRepository checkRepository;
    private final CheckGoodsRepository checkGoodsRepository;

    public void saveOrder(Long id, List<Goods> goodsList) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден по id " + id)));
        if (user.isPresent()) {
            UserEntity userEntity = user.get();

            Order order = new Order();

            order.setSellerId(userEntity.getId());
            order.setOrderTime(LocalDateTime.now());
            order.setStatus(Order.OrderStatus.ORDERED);

            for (Goods goods : goodsList) {
                OrderGoods orderGoods = new OrderGoods();
                orderGoods.setGoods(goods);
                orderGoods.setCount(goods.getCount());
                orderGoods.setOrder(order);
                orderGoods.setStatus(OrderGoods.GatherStatus.NOT_GATHERED);

                order.getGoodsWithCount().add(orderGoods);
            }

            orderRepository.save(order);
        }
    }

    public void createOrder(List<Integer> goodsIds,
                            Map<String, String> allParams) {
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

        saveOrder(userEntity.getId(), selectedGoods);

        selectedGoods.clear();
    }

    public void acceptGoods(List<Long> goodsIds,
                            Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order != null) {
            if (goodsIds != null) {
                for (Long id : goodsIds) {
                    OrderGoods selectedOrderGoods = orderGoodsRepository.findByOrderAndGoodsId(order, id);
                    if (selectedOrderGoods != null) {
                        selectedOrderGoods.setStatus(OrderGoods.GatherStatus.ACCEPTED);
                        orderGoodsRepository.save(selectedOrderGoods);

                        ShopGoods shopGoods = shopGoodsRepository.findByGoods(selectedOrderGoods.getGoods());
                        if (shopGoods == null) {
                            shopGoods = new ShopGoods();
                            shopGoods.setGoods(selectedOrderGoods.getGoods());
                            shopGoods.setCount(selectedOrderGoods.getCount());
                        } else {
                            shopGoods.setCount(shopGoods.getCount() + selectedOrderGoods.getCount());
                        }
                        shopGoodsRepository.save(shopGoods);
                    }
                }
            }
            order.setStatus(Order.OrderStatus.ACCEPTED);
            orderRepository.save(order);
        }
    }

    public void releaseGoods(Map<String, String> allParams) {
        List<String> itemCounts = allParams.keySet().stream()
                .filter(key -> key.startsWith("text_"))
                .toList();

        UserEntity userEntity = WebSecurityConfig.getUserEntity(userRepository);

        Check check = new Check();
        check.setSellerId(userEntity.getId());
        check.setSellTime(LocalDateTime.now());

        List<CheckGoods> checkGoodsList = new ArrayList<>();

        for (String itemCount : itemCounts) {
            long id = Long.parseLong(itemCount.split("_")[1]);
            long amount = Long.parseLong(allParams.get(itemCount));

            ShopGoods shopGoods = shopGoodsRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Товар не найден с id: " + id));

            if (amount > shopGoods.getCount()) {
                throw new IllegalArgumentException("В запросе товаров больше, чем присутствует в магазине");
            } else {
                shopGoods.setCount(shopGoods.getCount() - Math.toIntExact(amount));
                shopGoodsRepository.save(shopGoods);

                CheckGoods checkGoods = new CheckGoods();
                checkGoods.setCount((int)amount);
                checkGoods.setGoods(shopGoods.getGoods());
                checkGoods.setCheck(check);
                checkGoodsList.add(checkGoods);
            }
            check.setGoods(checkGoodsList);
            checkRepository.save(check);
        }
    }
}