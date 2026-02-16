package com.algaworks.algashop.ordering.domain.model.factory;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.entity.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;


class OrderFactoryTest {

    @Test
    void shouldGenerateDraftOrder() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        assertWith(order,
                o -> assertThat(o.id()).isNotNull(),
                o -> assertThat(o.customerId()).isEqualTo(customerId),
                o -> assertThat(o.totalAmount()).isEqualTo(Money.ZERO),
                o-> assertThat(o.totalItems()).isEqualTo(Quantity.ZERO),
                o -> assertThat(o.isDraft()).isTrue(),
                o -> assertThat(o.items()).isEmpty(),

                o -> assertThat(o.placedAt()).isNull(),
                o -> assertThat(o.paidAt()).isNull(),
                o -> assertThat(o.canceladAt()).isNull(),
                o -> assertThat(o.readyAt()).isNull(),
                o -> assertThat(o.billing()).isNull(),
                o -> assertThat(o.shipping()).isNull(),
                o -> assertThat(o.paymentMethod()).isNull());
    }

    @Test
    void shouldGenerateFilledOrderThatCanBePlaced() {
        Shipping shipping = OrderTestDataBuilder.aShipping();
        Billing billing = OrderTestDataBuilder.aBilling();

        Product product = ProductTestDataBuilder.aProduct();
        PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

        Quantity quantity = new Quantity(1);
        CustomerId customerId = new CustomerId();

        Order filledOrder = OrderFactory.filled(customerId, shipping, billing, paymentMethod, product, quantity);

        assertWith(filledOrder,
                o -> assertThat(o.shipping()).isEqualTo(shipping),
                o -> assertThat(o.billing()).isEqualTo(billing),
                o -> assertThat(o.paymentMethod()).isEqualTo(paymentMethod),
                o -> assertThat(o.totalItems()).isEqualTo(quantity),
                o -> assertThat(o.customerId()).isNotNull(),
                o -> assertThat(o.isDraft()).isTrue()
                );

        filledOrder.place();

        assertThat(filledOrder.isPlaced()).isTrue();
    }

}