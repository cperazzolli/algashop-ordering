package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity.OrderPersistenceEntityBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;


public class OrderPersistenceEntityTestDataBuilder {

    private OrderPersistenceEntityTestDataBuilder() {
    }

    public static OrderPersistenceEntityBuilder existingOrder() {
        return OrderPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customer(CustomerPersistenceEntityTestDataBuilder.aCustomer().build())
                .totalItems(3)
                .totalAmount(new BigDecimal(1250))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now())
                .readyAt(OffsetDateTime.now())
                .billing(billing().build())
                .shipping(aShipping().build())
                .items(
                        Set.of(
                                existingItem().build(),
                                existingItemAlt().build()
                        ));
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingItem() {
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .price(new BigDecimal(500))
                .quantity(2)
                .totalAmount(new BigDecimal(1000))
                .productName("Notebook")
                .productID(IdGenerator.generateTimeBasesUUID());
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingItemAlt() {
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .price(new BigDecimal(250))
                .quantity(1)
                .totalAmount(new BigDecimal(250))
                .productName("Mouse Pad")
                .productID(IdGenerator.generateTimeBasesUUID());
    }

    public static BillingEmbeddable.BillingEmbeddableBuilder billing() {
        return BillingEmbeddable.builder()
                .firstName("John")
                .lastName("Doe")
                .email("jhon.doe@email.com")
                .phone("11-4545-5454")
                .document("123456")
                .address(AddressEmbeddable.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .complement("Apt. 114")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode("12345")
                        .build());
    }

    public static ShippingEmbeddable.ShippingEmbeddableBuilder aShipping() {
        return ShippingEmbeddable.builder()
                .cost(new BigDecimal("500"))
                .expectedDate(LocalDate.now().plusDays(10))
                .recipient(RecipientEmbeddable.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .phone("11-4545-5454")
                        .document("123456")
                        .build())
                .address(AddressEmbeddable.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .complement("Apt. 114")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode("12345")
                        .build());
    }
}
