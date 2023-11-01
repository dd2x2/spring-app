package ru.dverkask.springapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Role {
    ADMIN("Администратор"),
    STOREKEEPER("Кладовщик"),
    SELLER("Продавец");

    @Getter
    private final String description;
}
