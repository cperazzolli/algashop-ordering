package com.algaworks.algashop.ordering.domain.valueobject;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public record BirthDate(LocalDate localDate) {

    public BirthDate(LocalDate localDate) {
        Objects.requireNonNull(localDate);
        if(localDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }
        this.localDate = localDate;
    }

    public Integer age() {
        return Period.between(this.localDate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return this.localDate.toString();
    }
}
