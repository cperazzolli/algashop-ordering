package com.algaworks.algashop.ordering.domain.model.entity;


import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;

class OrderItemTest {

    @Test
    void shouldCreateOrderItemTestBrandNewOrderItem() {
        OrderId orderId = new OrderId();
        Product product = ProductTestDataBuilder.aProduct();
        Quantity quantity = new Quantity(1);
        OrderItem orderItem = OrderItem.brandNewOrderItem()
                .orderId(orderId)
                .product(product)
                .quantity(quantity)
                .build();

        assertWith(orderItem,
                o -> assertThat(o.id()).isNotNull(),
                o -> assertThat(o.productId()).isEqualTo(product.id()),
                o -> assertThat(o.productName()).isEqualTo(product.name()),
                o -> assertThat(o.price()).isEqualTo(product.price()),
                o -> assertThat(o.quantity()).isEqualTo(quantity),
                o -> assertThat(o.orderId()).isEqualTo(orderId)
                );
        
    }
}