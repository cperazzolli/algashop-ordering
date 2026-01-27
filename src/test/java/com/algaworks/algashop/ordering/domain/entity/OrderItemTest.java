package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class OrderItemTest {

    @Test
    void shouldCreateOrderItemTest() {
        OrderItem.brandNewOrderItem()
                .orderId(new OrderId())
                .productId(new ProductId())
                .name(new ProductName("Product 1"))
                .price(new Money(BigDecimal.TEN))
                .quantity(new Quantity(10))
                .build();
    }
}