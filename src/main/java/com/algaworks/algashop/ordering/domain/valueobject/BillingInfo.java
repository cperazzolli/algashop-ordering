package com.algaworks.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record BillingInfo(
        FullName fullName,
        Document document,
        Phone phone,
        Address address
) {
    public BillingInfo(FullName fullName,Document document,Phone phone,Address address) {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
        Objects.requireNonNull(address);
        this.fullName = fullName;
        this.document = document;
        this.phone = phone;
        this.address = address;
    }
}
