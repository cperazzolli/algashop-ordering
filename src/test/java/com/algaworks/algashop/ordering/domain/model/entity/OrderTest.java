package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exception.OrderShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderStatusCannotBeChangedExcetpion;
import com.algaworks.algashop.ordering.domain.model.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertWith;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class OrderTest {

    @Test
    void shouldCreateOrderTest() {
       Order.draftOrder()
                .customerId(new CustomerId())
                .build();
    }

    @Test
    void shoultAddItem() {
        var order = Order.draftOrder().customerId(new CustomerId()).build();
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();
        ProductId productId = product.id();
        order.addItem(product, new Quantity(1));

        assertThat(order.items().size()).isEqualTo(1);

        OrderItem orderitem = order.items().iterator().next();

        assertWith(orderitem,
                i -> assertThat(i.id()).isNotNull(),
                i -> assertThat(i.productName()).isEqualTo(new ProductName("Mouse Pad")),
                i -> assertThat(i.productId()).isEqualTo(productId),
                i -> assertThat(i.price()).isEqualTo(new Money("200")),
                i -> assertThat(i.quantity()).isEqualTo(new Quantity(1))
                );
    }

    @Test
    void shouldGeneratedExceptionChangeOrderItens() {
        var order = Order.draftOrder().customerId(new CustomerId()).build();
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();
        order.addItem(product,
                new Quantity(2));


        Set<OrderItem> items = order.items();

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(items::clear);
    }

    @Test
    void shouldCalculatesTotal() {
        var order = Order.draftOrder().customerId(new CustomerId()).build();
        ProductId productId = new ProductId();
        order.addItem(ProductTestDataBuilder.aProductAltMousePad().build(),
                new Quantity(2));

        order.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(),
                new Quantity(1));


        assertThat(order.totalAmount()).isEqualTo(new Money("600"));
        assertThat(order.totalItems()).isEqualTo(new Quantity(3));
    }

    @Test
    void givenDraftOrder_whenPlace_shouldChangedToPlaced() {
        var order = OrderTestDataBuilder.anOrder().build();
        order.place();
        assertThat(order.isPlaced()).isTrue();
    }

    @Test
    void givenPaidOrder_whenMarkIsPaid_shouldChangeToPaid() {
      Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
      order.markAsPaid();
      assertThat(order.isPaid()).isTrue();
      assertThat(order.paidAt()).isNotNull();
    }

    @Test
    void givenPlaceOrder_whenTryToPlace_shouldGeneratedException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        assertThatExceptionOfType(OrderStatusCannotBeChangedExcetpion.class)
                .isThrownBy(order::place);
    }

    @Test
    void givenDraftOrder_whenChangePaymentMethod_shouldAllowChanged() {
       Order order = orderDraft();
       order.changedPaymentMethod(PaymentMethod.CREDIT_CARD);

       assertWith(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    void givenDraftOrder_whenChangeBilling_shouldAllowChanged() {
        Billing billing = OrderTestDataBuilder.aBilling();
        Order order = orderDraft();
        order.changeBilling(billing);

        assertWith(order.billing()).isEqualTo(billing);
    }

    @Test
    void givenDraftOrder_whenChangeShipping_shouldAllowChanged() {
        Shipping shipping = OrderTestDataBuilder.aShipping();

        Order order = orderDraft();

        order.changeShipping(shipping);

        assertWith(order,
                o -> assertThat(o.shipping()).isEqualTo(shipping));
    }

    @Test
    void givenDraftOrderAndDeliveryDateInThePast_whenChangeShipping_shouldNotAllowChanged() {
        LocalDate expectedDeliveryDate = LocalDate.now().minusDays(1);
        Shipping shipping = OrderTestDataBuilder.aShippingAlt().toBuilder()
                .expectedDate(expectedDeliveryDate)
                .build();

        Order order = orderDraft();

        assertThatExceptionOfType(OrderShippingDeliveryDateException.class)
                .isThrownBy(() -> order.changeShipping(shipping));

    }

    @Test
    void givenDraftOrder_whenChangeItem_shouldRecalculate() {
        Order order = orderDraft();
        order.addItem(ProductTestDataBuilder.aProduct(), new Quantity(3));
        OrderItem item = order.items().iterator().next();
        order.changeItemQuantity(item.id(), new Quantity(5));

        assertWith(order,
                o -> assertThat(o.totalAmount()).isEqualTo(new Money("15000")),
                o -> assertThat(o.totalItems()).isEqualTo(new Quantity(5))
                );
    }

    @Test
    void givenOutOfStockProduct_whenTryToAddToAnOrder_sholdNotAllow() {
        Order order = orderDraft();

        assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> order.addItem(ProductTestDataBuilder.aProductUnavailable().build(), new Quantity(1)));
    }

    Order orderDraft() {
        return Order.draft(new CustomerId());
    }
}