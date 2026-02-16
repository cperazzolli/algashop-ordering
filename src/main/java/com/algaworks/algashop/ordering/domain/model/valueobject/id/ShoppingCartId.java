package com.algaworks.algashop.ordering.domain.model.valueobject.id;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record ShoppingCartId(UUID id) {

    public ShoppingCartId {
        Objects.requireNonNull(id);
    }

    public ShoppingCartId() {
        this(IdGenerator.generateTimeBasesUUID());
    }

    @Override
    public String toString() {
        return  id.toString();
    }
}
