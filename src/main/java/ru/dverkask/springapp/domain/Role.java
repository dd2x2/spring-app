package ru.dverkask.springapp.domain;

import lombok.Getter;

public enum Role {
    ADMIN("Администратор"),
    STOREKEEPER("Кладовщик"),
    SELLER("Продавец");

    @Getter
    private final String description;
    Role(String description) {
        this.description = description;
    }
}
