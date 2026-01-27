package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;

import java.time.LocalDate;

public class OrderTestDataBuilder {

    private CustomerId customerId = new CustomerId();
    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    private Money shippingCost = new Money("10.00");
    private LocalDate expectedDeliveryDate = LocalDate.now().plusWeeks(1);
    private ShippingInfo shippingInfo = aShippingInfo();
    private BillingInfo billingInfo = aBillingInfo();

    private boolean withItems = true;
    private OrderStatus status = OrderStatus.DRAFT;


    private OrderTestDataBuilder() {
    }

    public static OrderTestDataBuilder anOrder() {
        return new OrderTestDataBuilder();
    }

    public Order build() {
        Order order = Order.draft(customerId);
        order.changeShipping(shippingInfo,shippingCost,expectedDeliveryDate);
        order.changeBilling(billingInfo);
        order.changedPaymentMethod(paymentMethod);

        if(withItems) {
            order.addItem(new ProductId(), new ProductName("Notebook x11"), new Money("3000"), new Quantity(2));
            order.addItem(new ProductId(), new ProductName("RAM 4 GB"), new Money("200"), new Quantity(1));
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

    public static ShippingInfo aShippingInfo() {
        return ShippingInfo.builder()
                .address(address())
                .document(new Document("571.168.240-73"))
                .phone(new Phone("11-4545-5454"))
                .fullName(new FullName("John", "Doe"))
                .build();
    }

    public static BillingInfo aBillingInfo() {
        return BillingInfo.builder()
                .address(address())
                .document(new Document("571.168.240-73"))
                .phone(new Phone("11-4545-5454"))
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

    public OrderTestDataBuilder customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestDataBuilder paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderTestDataBuilder shippingCost(Money shippingCost) {
        this.shippingCost = shippingCost;
        return this;
    }

    public OrderTestDataBuilder expectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
        return this;
    }

    public OrderTestDataBuilder shippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
        return this;
    }

    public OrderTestDataBuilder billingInfo(BillingInfo billingInfo) {
        this.billingInfo = billingInfo;
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
