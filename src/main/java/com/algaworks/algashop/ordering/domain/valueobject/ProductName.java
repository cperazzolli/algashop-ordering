package com.algaworks.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record ProductName(String name) {

    public ProductName(String name) {
        Objects.requireNonNull(name);
        if(name.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }
}
