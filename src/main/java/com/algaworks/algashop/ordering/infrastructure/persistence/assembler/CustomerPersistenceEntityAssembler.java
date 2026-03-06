package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityAssembler {

    public CustomerPersistenceEntity fromDomain(Customer customer) {
        return merge(new CustomerPersistenceEntity(),customer);
    }

    public CustomerPersistenceEntity merge(CustomerPersistenceEntity customerPersistenceEntity,Customer customer) {
        customerPersistenceEntity.setId(customer.id().value());
        customerPersistenceEntity.setBilling(billingFrom(customer));
        customerPersistenceEntity.setBirthDate(customer.birthDate().localDate());
        customerPersistenceEntity.setPromotionNotificationsAllowed(customer.isPrommotionNotificationsAllowed());
        customerPersistenceEntity.setArchived(customer.isArchived());
        customerPersistenceEntity.setRegistradAt(customer.registradAt());
        customerPersistenceEntity.setArchivedAt(customer.quiveredAt());
        customerPersistenceEntity.setLoyaltyPoints(customer.loyaltyPoints().value());
        return customerPersistenceEntity;
    }

    public AddressEmbeddable addressEmbeddable(Address address) {
        return new AddressEmbeddable(
                address.street(),
                address.number(),
                address.complement(),
                address.neighborhood(),
                address.city(),
                address.state(),
                address.zipCode().value()
        );
    }

    public BillingEmbeddable billingFrom(Customer billing) {
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
        billingEmbeddable.setEmail(billing.email().email());
        billingEmbeddable.setAddress(addressEmbeddable);

        return billingEmbeddable;
    }
}
