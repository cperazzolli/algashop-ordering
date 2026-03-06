package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.repository.Customers;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disasembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerPersistenceProvider implements Customers {

    private final CustomerPersistenceEntityRepository persistenceRepository;
    private final CustomerPersistenceEntityAssembler assembler;
    private final CustomerPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;

    @Override
    public Optional<Customer> ofId(CustomerId customerId) {
        var possibleEntity = persistenceRepository
                .findById(customerId.value());
        return possibleEntity.map(disassembler::toDomainCustomer);
    }

    @Override
    public Boolean existsById(CustomerId customerId) {
        return persistenceRepository.existsById(customerId.value());
    }

    @Override
    public void add(Customer aggregateRoot) {
        UUID aLong = aggregateRoot.id().value();
        persistenceRepository.findById(aLong)
                .ifPresentOrElse(
                        (persistenceEntity) -> onUpdate(persistenceEntity,aggregateRoot),
                        () -> onCreate(aggregateRoot));
    }

    @Override
    public Long count() {
        return persistenceRepository.count();
    }

    private void onCreate(Customer aggregateRoot) {
        var persistenceEntity = assembler.fromDomain(aggregateRoot);
        persistenceRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot,persistenceEntity);
    }

    private void onUpdate(CustomerPersistenceEntity persistenceEntity, Customer aggregateRoot) {
        persistenceEntity = assembler.merge(persistenceEntity, aggregateRoot);
        entityManager.detach(persistenceEntity);
        persistenceEntity = persistenceRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot,persistenceEntity);
    }

    @SneakyThrows
    private void updateVersion(Customer aggregateRoot, CustomerPersistenceEntity persistenceEntity) {
        Field version = aggregateRoot.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version,aggregateRoot, persistenceEntity.getVersion());
        version.setAccessible(false);
    }

}
