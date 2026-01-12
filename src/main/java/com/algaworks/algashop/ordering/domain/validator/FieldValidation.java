package com.algaworks.algashop.ordering.domain.validator;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class FieldValidation {

    private FieldValidation() {

    }

    public static void requiredValidEmail(String email) {
        requiredValidEmail(email,null);
    }

    public static void requiredValidEmail(String email, String errorMessage) {
       Objects.requireNonNull(email,errorMessage);
       if(email.isBlank()) {
           throw new IllegalArgumentException(errorMessage);
       }
       if (!EmailValidator.getInstance().isValid(email)) {
           throw new IllegalArgumentException(errorMessage);
       }
    }
}
