package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.repository.Orders;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class OrdersPersistenceprovider implements Orders {

    private final OrderPersistenceEntityRepository orderPersistenceRepository;

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        return Optional.empty();
    }

    @Override
    public Boolean existsById(OrderId orderId) {
        return null;
    }

    @Override
    public void add(Order aggregateRoot) {
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntity.builder()
                .id(aggregateRoot.id().value().toLong())
                .customerId(aggregateRoot.customerId().value())
                .build();

        orderPersistenceRepository.saveAndFlush(persistenceEntity);
    }

    @Override
    public int count() {
        return 0;
    }
}
