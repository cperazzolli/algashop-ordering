package com.algaworks.algashop.ordering.infrastructure.persistence.disasembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class OrderPersistenceEntityDisassembler {

    public Order toDomainOrder(OrderPersistenceEntity orderPersistenceEntity) {
       return Order.existing()
               .id(new OrderId(orderPersistenceEntity.getId()))
               .customerId(new CustomerId(orderPersistenceEntity.getCustomerId()))
               .totalAmount(new Money(orderPersistenceEntity.getTotalAmount()))
               .totalItems(new Quantity(orderPersistenceEntity.getTotalItems()))
               .status(OrderStatus.valueOf(orderPersistenceEntity.getStatus()))
               .paymentMethod(PaymentMethod.valueOf(orderPersistenceEntity.getPaymentMethod()))
               .placedAt(orderPersistenceEntity.getPlacedAt())
               .paidAt(orderPersistenceEntity.getPaidAt())
               .canceledAt(orderPersistenceEntity.getCanceledAt())
               .readyAt(orderPersistenceEntity.getReadyAt())
               .billing(billingFrom(orderPersistenceEntity.getBilling()))
               .shipping(shippingFrom(orderPersistenceEntity.getShipping()))
               .items(new HashSet<>())
               .version(orderPersistenceEntity.getVersion())
               .build();
    }

    public Billing billingFrom(BillingEmbeddable billingEmbeddable) {
        return new Billing(
                new FullName(billingEmbeddable.getLastName(),
                        billingEmbeddable.getLastName()),
                new Document(billingEmbeddable.getDocument()),
                new Phone(billingEmbeddable.getPhone()),
                new Email(billingEmbeddable.getEmail()),
                new Address(billingEmbeddable.getAddress().getStreet(),
                        billingEmbeddable.getAddress().getNumber(),
                        billingEmbeddable.getAddress().getComplement(),
                        billingEmbeddable.getAddress().getNeighborhood(),
                        billingEmbeddable.getAddress().getCity(),
                        billingEmbeddable.getAddress().getState(),
                        new ZipCode(billingEmbeddable.getAddress().getZipCode())

                )
        );
    }
    public Shipping shippingFrom(ShippingEmbeddable shippingEmbeddable) {
        return new Shipping(new Money(shippingEmbeddable.getCost()),
                shippingEmbeddable.getExpectedDate(),
                new Recipient(new FullName(shippingEmbeddable.getRecipient().getFirstName(),
                        shippingEmbeddable.getRecipient().getLastName()),
                        new Document(shippingEmbeddable.getRecipient().getDocument()),
                        new Phone(shippingEmbeddable.getRecipient().getPhone())),
                new Address(shippingEmbeddable.getAddress().getStreet(),
                        shippingEmbeddable.getAddress().getNumber(),
                        shippingEmbeddable.getAddress().getComplement(),
                        shippingEmbeddable.getAddress().getNeighborhood(),
                        shippingEmbeddable.getAddress().getCity(),
                        shippingEmbeddable.getAddress().getState(),
                        new ZipCode(shippingEmbeddable.getAddress().getZipCode())
                )
        );
    }
}
