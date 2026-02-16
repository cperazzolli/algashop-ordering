package com.algaworks.algashop.ordering.domain.model.valueobject;

import com.algaworks.algashop.ordering.domain.model.validator.FieldValidation;


public record Email(String email) {

    public Email(String email) {
        this.email = FieldValidation.requiredValidEmail(email);
    }

    @Override
    public String toString() {
        return email;
    }
}
