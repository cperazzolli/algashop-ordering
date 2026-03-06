package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of="id")
@Table(name = "\"cutomer\"")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class CustomerPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private LocalDate birthDate;
    private Boolean promotionNotificationsAllowed;
    private Boolean archived;
    private OffsetDateTime registradAt;
    private OffsetDateTime archivedAt;
    private Integer loyaltyPoints;

    @Version
    private Long version;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "billing_first_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "billing_last_name")),
            @AttributeOverride(name = "document", column = @Column(name = "billing_document")),
            @AttributeOverride(name = "phone", column = @Column(name = "billing_phone")),
            @AttributeOverride(name = "address.street", column = @Column(name = "billing_address_street")),
            @AttributeOverride(name = "address.number", column = @Column(name = "billing_address_number")),
            @AttributeOverride(name = "address.complement", column = @Column(name = "billing_address_complement")),
            @AttributeOverride(name = "address.neighborhood", column = @Column(name = "billing_address_neighborhood")),
            @AttributeOverride(name = "address.city", column = @Column(name = "billing_address_city")),
            @AttributeOverride(name = "address.state", column = @Column(name = "billing_address_state")),
            @AttributeOverride(name = "address.zipCode", column = @Column(name = "billing_address_zipCode"))
    })
    private BillingEmbeddable billing;

    @CreatedBy
    private UUID createdByUserId;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    public Long version() {
        return version;
    }

    private void setVersion(Long version) {
        this.version = version;
    }

}
