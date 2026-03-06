package com.algaworks.algashop.ordering.infrastructure.persistence.disasembler;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityDisassembler {

    public Customer toDomainCustomer(CustomerPersistenceEntity customerPersistenceEntity) {
        return Customer.existing()
                .id(new CustomerId(customerPersistenceEntity.getId()))
                .fullName(new FullName(customerPersistenceEntity.getBilling().getLastName(), customerPersistenceEntity.getBilling().getFirstName()))
                .birthDate(new BirthDate(customerPersistenceEntity.getBirthDate()))
                .email(new Email(customerPersistenceEntity.getBilling().getEmail()))
                .phone(new Phone(customerPersistenceEntity.getBilling().getPhone()))
                .document(new Document(customerPersistenceEntity.getBilling().getDocument()))
                .prommotionNotificationsAllowed(customerPersistenceEntity.getPromotionNotificationsAllowed())
                .archived(customerPersistenceEntity.getArchived())
                .registradAt(customerPersistenceEntity.getRegistradAt())
                .archivedAt(customerPersistenceEntity.getArchivedAt())
                .loyaltyPoints(new LoyaltyPoints(customerPersistenceEntity.getLoyaltyPoints()))
                .address(toDomainAddress(customerPersistenceEntity.getBilling().getAddress()))
                .build();

    }

    public Address toDomainAddress(AddressEmbeddable addressEmbeddable) {
        return new Address(
                addressEmbeddable.getStreet(),
                addressEmbeddable.getNumber(),
                addressEmbeddable.getComplement(),
                addressEmbeddable.getNeighborhood(),
                addressEmbeddable.getCity(),
                addressEmbeddable.getState(),
                new ZipCode(addressEmbeddable.getZipCode())
        );
    }
}
