package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "shopping_cart_item")
@Data
@ToString(of= "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartItemPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private UUID productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private Boolean available;
    private BigDecimal totalAmount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCartPersistenceEntity shoppingCart;

    public UUID getShoppingCartId() {
        return shoppingCart == null ? null : shoppingCart.getId();
    }
}
