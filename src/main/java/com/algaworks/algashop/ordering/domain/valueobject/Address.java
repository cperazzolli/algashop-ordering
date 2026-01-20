package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.validator.FieldValidation;
import lombok.Builder;

import java.util.Objects;

public record Address(
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        ZipCode zipCode
) {
    @Builder(toBuilder = true)
    public Address {
        FieldValidation.requeridNonBlanck(street);
        FieldValidation.requeridNonBlanck(neighborhood);
        FieldValidation.requeridNonBlanck(city);
        FieldValidation.requeridNonBlanck(number);
        FieldValidation.requeridNonBlanck(state);
        Objects.requireNonNull(zipCode);
    }
}
