package com.algaworks.algashop.ordering.infrastructure.persistence.disasembler;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@AllArgsConstructor
public class ShoppingCartPersistenceEntityDisassembler {

    public ShoppingCart todomainEntity(ShoppingCartPersistenceEntity shoppingCartPersistenceEntity) {
        return ShoppingCart.existing()
                .id(new ShoppingCartId(shoppingCartPersistenceEntity.getId()))
                .customerId(new CustomerId(shoppingCartPersistenceEntity.getCustomer().getId()))
                .totalAmount(new Money(shoppingCartPersistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(shoppingCartPersistenceEntity.getTotalItems()))
                .createdAt(shoppingCartPersistenceEntity.getCreatedAt())
                .items(new HashSet<>())
                .build();
    }
}
