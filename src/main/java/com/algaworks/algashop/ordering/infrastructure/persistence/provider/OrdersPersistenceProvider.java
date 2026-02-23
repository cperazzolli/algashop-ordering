package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.repository.Orders;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disasembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository persistenceRepository;
    private final OrderPersistenceEntityAssembler assembler;
    private final OrderPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        var possibleEntity = persistenceRepository
                .findById(orderId.value().toLong());
        return possibleEntity.map(disassembler::toDomainOrder);
    }

    @Override
    public Boolean existsById(OrderId orderId) {
        return null;
    }

    @Override
    public void add(Order aggregateRoot) {
        long aLong = aggregateRoot.id().value().toLong();
        persistenceRepository.findById(aLong)
                .ifPresentOrElse(
                        (persistenceEntity) -> {
                            onUpdate(persistenceEntity,aggregateRoot);
                            },
                        () -> {
                            onCreate(aggregateRoot);
                });

    }

    private void onCreate(Order aggregateRoot) {
        OrderPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
        persistenceRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot,persistenceEntity);
    }

    private void onUpdate(OrderPersistenceEntity persistenceEntity, Order aggregateRoot) {
        persistenceEntity = assembler.merge(persistenceEntity, aggregateRoot);
        entityManager.detach(persistenceEntity);
        persistenceEntity = persistenceRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot,persistenceEntity);
    }

    @SneakyThrows
    private void updateVersion(Order aggregateRoot, OrderPersistenceEntity persistenceEntity) {
        Field version = aggregateRoot.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version,aggregateRoot, persistenceEntity.getVersion());
        version.setAccessible(false);
    }

    @Override
    public int count() {
        return 0;
    }
}
