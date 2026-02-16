package com.algaworks.algashop.ordering.domain.model.validator;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class FieldValidation {

    private FieldValidation() {

    }

    public static void requeridNonBlanck(String value) {
        requeridNonBlanck(value,"");
    }

    public static void requeridNonBlanck(String value, String errorMessage) {
       Objects.requireNonNull(value);
       if(value.isBlank()) {
           throw new IllegalArgumentException(errorMessage);
       }
    }

    public static String requiredValidEmail(String email) {
       return requiredValidEmail(email,null);
    }

    public static String requiredValidEmail(String email, String errorMessage) {
       Objects.requireNonNull(email,errorMessage);
       if(email.isBlank()) {
           throw new IllegalArgumentException(errorMessage);
       }
       if (!EmailValidator.getInstance().isValid(email)) {
           throw new IllegalArgumentException(errorMessage);
       }
       return email;
    }
}
