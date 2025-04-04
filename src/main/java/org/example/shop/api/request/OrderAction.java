package org.example.shop.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum OrderAction {
    PLUS_ITEM("plus"),
    MINUS_ITEM("minus"),
    DELETE_ITEM("delete");

    private final String value;

    public static OrderAction getActionByName(String name) {
        return Arrays.stream(values())
                .filter(item -> item.value.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("action not found"));
    }
}
