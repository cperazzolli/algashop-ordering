package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SpringDataAuditingConfig.class)
class OrderPersistenceEntityRepositoryIT {

    private final OrderPersistenceEntityRepository orderPersistenceEntityRepository;

    @Autowired
    OrderPersistenceEntityRepositoryIT(OrderPersistenceEntityRepository orderPersistenceEntityRepository) {
        this.orderPersistenceEntityRepository = orderPersistenceEntityRepository;
    }


    @Test
    void shouldPersist() {
        OrderPersistenceEntity entity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();
        orderPersistenceEntityRepository.saveAndFlush(entity);
        assertThat(orderPersistenceEntityRepository.existsById(entity.getId())).isTrue();

        OrderPersistenceEntity savedEntity = orderPersistenceEntityRepository.findById(entity.getId()).orElseThrow();

        assertThat(savedEntity.getItems()).isNotEmpty();

    }

    @Test
    void shouldCount() {
        long count = orderPersistenceEntityRepository.count();
        assertThat(count).isZero();
    }

    @Test
    void shouldTestAuditingValues() {
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();
        persistenceEntity = orderPersistenceEntityRepository.saveAndFlush(persistenceEntity);

        assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();
        assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();
    }
}