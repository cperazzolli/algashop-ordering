package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderPersistenceEntityAssembler {

    public OrderPersistenceEntity fromDomain(Order order) {
        return merge(new OrderPersistenceEntity(), order);
    }

    public OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        orderPersistenceEntity.setId(order.id().value().toLong());
        orderPersistenceEntity.setCustomerId(order.customerId().value());
        orderPersistenceEntity.setTotalAmount(order.totalAmount().value());
        orderPersistenceEntity.setTotalItems(order.totalItems().value());
        orderPersistenceEntity.setStatus(order.status().name());
        orderPersistenceEntity.setPaymentMethod(order.paymentMethod().name());
        orderPersistenceEntity.setPlacedAt(order.placedAt());
        orderPersistenceEntity.setCanceledAt(order.canceladAt());
        orderPersistenceEntity.setPaidAt(order.paidAt());
        orderPersistenceEntity.setReadyAt(order.readyAt());
        orderPersistenceEntity.setVersion(order.version());
        orderPersistenceEntity.setShipping(shippingFrom(order.shipping()));
        orderPersistenceEntity.setBilling(billingFrom(order.billing()));
        Set<OrderItemPersistenceEntity> mergedItems = mergeItems(order,orderPersistenceEntity);
        orderPersistenceEntity.replaceItems(mergedItems);
        return orderPersistenceEntity;
    }

    private Set<OrderItemPersistenceEntity> mergeItems(Order order, OrderPersistenceEntity orderPersistenceEntity) {
        Set<OrderItem> newOrUpdateItems = order.items();
        if (newOrUpdateItems == null || newOrUpdateItems.isEmpty()) {
            return new HashSet<>();
        }

        Set<OrderItemPersistenceEntity> existingItems = orderPersistenceEntity.getItems();

        if(existingItems == null || existingItems.isEmpty()) {
            var resultItems = newOrUpdateItems.stream()
                    .map(orderItem -> fromDomain(orderItem))
                    .collect(Collectors.toSet());
            return resultItems;
        }

        Map<Long, OrderItemPersistenceEntity> existingItemMap = existingItems.stream()
                .collect(Collectors.toMap(OrderItemPersistenceEntity::getId, item -> item));

        return newOrUpdateItems.stream()
                .map(orderItem -> {
                    OrderItemPersistenceEntity itemPersistence = existingItemMap.getOrDefault(
                            orderItem.id().value().toLong(), new OrderItemPersistenceEntity()
                    );
                    return merge(itemPersistence, orderItem);
                }).collect(Collectors.toSet());
    }

    public OrderItemPersistenceEntity fromDomain(OrderItem orderItem) {
        return merge(new OrderItemPersistenceEntity(), orderItem);
    }

    private OrderItemPersistenceEntity merge(OrderItemPersistenceEntity orderItemPersistenceEntity, OrderItem orderItem) {
        orderItemPersistenceEntity.setId(orderItem.id().value().toLong());
        orderItemPersistenceEntity.setProductID(orderItem.productId().value());
        orderItemPersistenceEntity.setProductName(orderItemPersistenceEntity.getProductName());
        orderItemPersistenceEntity.setPrice(orderItem.price().value());
        orderItemPersistenceEntity.setQuantity(orderItem.quantity().value());
        orderItemPersistenceEntity.setTotalAmount(orderItem.totalAmount().value());
        return orderItemPersistenceEntity;
    }

    public BillingEmbeddable billingFrom(Billing billing) {
        BillingEmbeddable billingEmbeddable = new BillingEmbeddable();
        AddressEmbeddable addressEmbeddable = new AddressEmbeddable();
        addressEmbeddable.setStreet(billing.address().street());
        addressEmbeddable.setNumber(billing.address().number());
        addressEmbeddable.setComplement(billing.address().complement());
        addressEmbeddable.setNeighborhood(billing.address().neighborhood());
        addressEmbeddable.setCity(billing.address().city());
        addressEmbeddable.setState(billing.address().state());
        addressEmbeddable.setZipCode(billing.address().zipCode().value());
        billingEmbeddable.setFirstName(billing.fullName().firstName());
        billingEmbeddable.setLastName(billing.fullName().lastName());
        billingEmbeddable.setPhone(billing.phone().phone());
        billingEmbeddable.setDocument(billing.document().document());
        billingEmbeddable.setAddress(addressEmbeddable);

        return billingEmbeddable;
    }
    public ShippingEmbeddable shippingFrom(Shipping shipping) {
        ShippingEmbeddable shippingEmbeddable = new ShippingEmbeddable();
        AddressEmbeddable addressEmbeddable = new AddressEmbeddable();
        RecipientEmbeddable recipientEmbeddable = new RecipientEmbeddable();
        addressEmbeddable.setStreet(shipping.address().street());
        addressEmbeddable.setNumber(shipping.address().number());
        addressEmbeddable.setComplement(shipping.address().complement());
        addressEmbeddable.setNeighborhood(shipping.address().neighborhood());
        addressEmbeddable.setCity(shipping.address().city());
        addressEmbeddable.setState(shipping.address().state());
        addressEmbeddable.setZipCode(shipping.address().zipCode().value());
        recipientEmbeddable.setFirstName(shipping.recipient().fullName().firstName());
        recipientEmbeddable.setLastName(shipping.recipient().fullName().lastName());
        recipientEmbeddable.setPhone(shipping.recipient().phone().phone());
        recipientEmbeddable.setDocument(shipping.recipient().document().document());
        shippingEmbeddable.setCost(shipping.cost().value());
        shippingEmbeddable.setExpectedDate(shipping.expectedDate());
        shippingEmbeddable.setAddress(addressEmbeddable);
        shippingEmbeddable.setRecipient(recipientEmbeddable);
        return shippingEmbeddable;
    }

}
