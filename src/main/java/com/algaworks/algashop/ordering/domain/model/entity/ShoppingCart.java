package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

public class ShoppingCart {

    private ShoppingCartId id;
    private CustomerId customerId;
    private Money amount;
    private Quantity totalItems;
    private OffsetDateTime createdAt;

    private Set<OrderItem> items;

    @Builder(builderClassName = "ExistingShoppingBuild", builderMethodName = "existing")
    public ShoppingCart(ShoppingCartId id, CustomerId customerId, Money amount, Quantity totalItems, OffsetDateTime createdAt) {
        setId(id);
        setCustomerId(customerId);
        setAmount(amount);
        setTotalItems(totalItems);
        setCreatedAt(createdAt);
    }

    @Builder(builderClassName = "startShoppingBuilder", builderMethodName = "startShopping")
    public static ShoppingCart startShopping(CustomerId customerId) {
      return new ShoppingCart(
              new ShoppingCartId(),
              new CustomerId(),
              Money.ZERO,
              Quantity.ZERO,
              null
      );
    }

    public void addItem(Product product, Quantity quantity) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);
        product.chackOutOfStock();
        setId(id());
        setCustomerId(customerId());
        setAmount(product.price());
        setTotalItems(quantity);
        setCreatedAt(OffsetDateTime.now());
    }


    private void recalculateTotal() {
        this.setAmount(this.amount.multiply(this.totalItems()));
    }


    public ShoppingCartId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Money amount() {
        return amount;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public void setId(ShoppingCartId id) {
        this.id = id;
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public void setTotalItems(Quantity totalItems) {
        this.totalItems = totalItems;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<OrderItem> items() {
        return items;
    }

    private void setItems(Set<OrderItem> items) {
        this.items = items;
    }
}
