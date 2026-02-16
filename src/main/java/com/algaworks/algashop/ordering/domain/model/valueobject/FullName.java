package com.algaworks.algashop.ordering.domain.model.valueobject;

import java.util.Objects;

public record FullName(String lastName, String firstName) {

    public FullName(String lastName, String firstName) {
        Objects.requireNonNull(lastName);
        Objects.requireNonNull(firstName);

        if(firstName.isBlank()) {
            throw new IllegalArgumentException();
        }

        if(lastName.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.lastName = lastName.trim();
        this.firstName = firstName.trim();
    }

    @Override
    public String toString() {
        return String.format("%s %s", firstName, lastName);
    }
}
