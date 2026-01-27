package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.OrderShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedExcetpion;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
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
        ProductId productId = new ProductId();
        order.addItem(productId, new ProductName("Mouse Pad"), new Money("100"), new Quantity(10));

        assertThat(order.items().size()).isEqualTo(1);

        OrderItem orderitem = order.items().iterator().next();

        assertWith(orderitem,
                i -> assertThat(i.id()).isNotNull(),
                i -> assertThat(i.name()).isEqualTo(new ProductName("Mouse Pad")),
                i -> assertThat(i.productId()).isEqualTo(productId),
                i -> assertThat(i.unitPrice()).isEqualTo(new Money("100")),
                i -> assertThat(i.quantity()).isEqualTo(new Quantity(10))
                );
    }

    @Test
    void shouldGeneratedExceptionChangeOrderItens() {
        var order = Order.draftOrder().customerId(new CustomerId()).build();
        ProductId productId = new ProductId();
        order.addItem(productId,
                new ProductName("Mouse Pad"),
                new Money("100"),
                new Quantity(2));


        Set<OrderItem> items = order.items();

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(items::clear);
    }

    @Test
    void shouldCalculatesTotal() {
        var order = Order.draftOrder().customerId(new CustomerId()).build();
        ProductId productId = new ProductId();
        order.addItem(productId,
                new ProductName("Mouse Pad"),
                new Money("100"),
                new Quantity(2));

        order.addItem(productId,
                new ProductName("Mouse Pad"),
                new Money("50"),
                new Quantity(1));


        assertThat(order.totalAmount()).isEqualTo(new Money("250"));
        assertThat(order.quantity()).isEqualTo(new Quantity(3));
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
    void givenPlaceOrder_whenTryToPlace_shouldGeneratedExcetpion() {
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
    void givenDraftOrder_whenChangeBillingInfo_shouldAllowChanged() {
        Address address = Address.builder()
                .street("Bourbon Street")
                .number("123456")
                .neighborhood("North Ville")
                .complement("Apt. 11")
                .city("Montfort")
                .state("South California")
                .zipCode(new ZipCode("79911"))
                .build();

        BillingInfo billing = BillingInfo.builder()
                .address(address)
                .document(new Document("571.168.240-73"))
                .phone(new Phone("11-4545-5454"))
                .fullName(new FullName("John", "Doe"))
                .build();
        Order order = orderDraft();
        order.changeBilling(billing);

        assertWith(order.billingInfo()).isEqualTo(billing);
    }

    @Test
    void givenDraftOrder_whenChangeShippingInfo_shouldAllowChanged() {
        Address address = Address.builder()
                .street("Bourbon Street")
                .number("123456")
                .neighborhood("North Ville")
                .complement("Apt. 11")
                .city("Montfort")
                .state("South California")
                .zipCode(new ZipCode("79911"))
                .build();

        ShippingInfo shippingInfo = ShippingInfo.builder()
                .address(address)
                .document(new Document("571.168.240-73"))
                .phone(new Phone("11-4545-5454"))
                .fullName(new FullName("John", "Doe"))
                .build();

        Order order = orderDraft();
        Money shippingCost = Money.ZERO;
        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(1);

        order.changeShipping(shippingInfo,shippingCost,expectedDeliveryDate);

        assertWith(order,
                o -> assertThat(o.shippingInfo()).isEqualTo(shippingInfo),
                o -> assertThat(o.shippingCost()).isEqualTo(shippingCost),
                o -> assertThat(o.expectedDeliveryDate()).isEqualTo(expectedDeliveryDate));
    }

    @Test
    void givenDraftOrderAndDeliveryDateInThePast_whenChangeShippingInfo_shouldNotAllowChanged() {
        Address address = Address.builder()
                .street("Bourbon Street")
                .number("123456")
                .neighborhood("North Ville")
                .complement("Apt. 11")
                .city("Montfort")
                .state("South California")
                .zipCode(new ZipCode("79911"))
                .build();

        ShippingInfo shippingInfo = ShippingInfo.builder()
                .address(address)
                .document(new Document("571.168.240-73"))
                .phone(new Phone("11-4545-5454"))
                .fullName(new FullName("John", "Doe"))
                .build();

        Order order = orderDraft();
        Money shippingCost = Money.ZERO;
        LocalDate expectedDeliveryDate = LocalDate.now().minusDays(1);

        assertThatExceptionOfType(OrderShippingDeliveryDateException.class)
                .isThrownBy(() -> order.changeShipping(shippingInfo,shippingCost,expectedDeliveryDate));

    }

    @Test
    void givenDraftOrder_whenChangeItem_shouldRecalculate() {
        Order order = orderDraft();
        order.addItem(new ProductId(), new ProductName("Desktop x11"), new Money("10.00"), new Quantity(3));
        OrderItem item = order.items().iterator().next();
        order.changeItemQuantity(item.id(), new Quantity(5));

        assertWith(order,
                o -> assertThat(o.totalAmount()).isEqualTo(new Money("50.00")),
                o -> assertThat(o.quantity()).isEqualTo(new Quantity(5))
                );
    }

    Order orderDraft() {
        return Order.draft(new CustomerId());
    }
}