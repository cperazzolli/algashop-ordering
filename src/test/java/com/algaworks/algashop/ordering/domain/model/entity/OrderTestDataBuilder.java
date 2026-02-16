package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;

import java.time.LocalDate;

public class OrderTestDataBuilder {

    private CustomerId customerId = new CustomerId();
    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    private Shipping shipping = aShipping();
    private Billing billing = aBilling();

    private boolean withItems = true;
    private OrderStatus status = OrderStatus.DRAFT;


    private OrderTestDataBuilder() {
    }

    public static OrderTestDataBuilder anOrder() {
        return new OrderTestDataBuilder();
    }

    public Order build() {
        Order order = Order.draft(customerId);
        order.changeShipping(shipping);
        order.changeBilling(billing);
        order.changedPaymentMethod(paymentMethod);

        if(withItems) {
            order.addItem(ProductTestDataBuilder.aProduct(), new Quantity(2));
            order.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));
        }

        switch (this.status) {
            case DRAFT -> {
                order.isDraft();
            }
            case PLACED -> {
                order.place();
            }
            case PAID -> {
                order.markAsPaid();
            }
            case READY -> {

            }
            case CANCELED -> {

            }
        }
        return order;
    }

    public static Shipping aShipping() {
        return Shipping.builder()
                .cost(new Money("10"))
                .expectedDate(LocalDate.now().plusDays(1))
                .recipient(Recipient.builder()
                        .document(new Document("571.168.240-73"))
                        .phone(new Phone("11-4545-5454"))
                        .fullName(new FullName("John", "Doe"))
                        .build())
                .address(address())
                .build();
    }

    public static Shipping aShippingAlt() {
        return Shipping.builder()
                .cost(new Money("20.00"))
                .expectedDate(LocalDate.now().plusDays(2))
                .address(addressAlt())
                .recipient(Recipient.builder()
                        .fullName(new FullName("Mary", "Jones"))
                        .document(new Document("123.456.789-00"))
                        .phone(new Phone("11-4545-5454"))
                        .build())
                .build();
    }
    public static Billing aBilling() {
        return Billing.builder()
                .address(address())
                .document(new Document("571.168.240-73"))
                .phone(new Phone("11-4545-5454"))
                .email(new Email("john.doe@email.com"))
                .fullName(new FullName("John", "Doe"))
                .build();
    }

    public static Address address() {
        return Address.builder()
                .street("Bourbon Street")
                .number("123456")
                .neighborhood("North Ville")
                .complement("Apt. 11")
                .city("Montfort")
                .state("South California")
                .zipCode(new ZipCode("79911"))
                .build();
    }

    public static Address addressAlt() {
        return Address.builder()
                .street("Sansome Street")
                .number("875")
                .neighborhood("Sansome")
                .city("San Francisco")
                .state("California")
                .zipCode(new ZipCode("08040"))
                .build();
    }

    public OrderTestDataBuilder customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestDataBuilder paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderTestDataBuilder shipping(Shipping shipping) {
        this.shipping = shipping;
        return this;
    }

    public OrderTestDataBuilder billing(Billing billing) {
        this.billing = billing;
        return this;
    }

    public OrderTestDataBuilder withItems(boolean withItems) {
        this.withItems = withItems;
        return this;
    }

    public OrderTestDataBuilder status(OrderStatus status) {
        this.status = status;
        return this;
    }

}
