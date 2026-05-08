package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Table(name = "\"shopping_cart\"")
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ShoppingCartPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private CustomerPersistenceEntity customer;

    private BigDecimal totalAmount;
    private Integer totalItems;
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShoppingCartItemPersistenceEntity> items = new HashSet<>();

    @Builder
    public ShoppingCartPersistenceEntity(UUID id, CustomerPersistenceEntity customer, BigDecimal totalAmount, Integer totalItems, OffsetDateTime createdAt,
                                         UUID createdByUserId, OffsetDateTime lastModifiedAt, UUID lastModifiedByUserId, Long version, Set<ShoppingCartItemPersistenceEntity> items) {
        this.id = id;
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.createdAt = createdAt;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAt = lastModifiedAt;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
        this.replaceItems(items);
    }

    public void replaceItems(Set<ShoppingCartItemPersistenceEntity> items) {
        if (items == null || items.isEmpty()) {
            this.setItems(new HashSet<>());
            return;
        }

        items.forEach(i -> i.setShoppingCart(this));
        this.setItems(items);
    }

    @Version
    private Long version;

    @CreatedBy
    private UUID createdByUserId;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    @LastModifiedBy
    private UUID lastModifiedByUserId;
}
