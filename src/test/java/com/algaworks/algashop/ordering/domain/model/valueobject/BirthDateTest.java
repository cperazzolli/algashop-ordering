package com.algaworks.algashop.ordering.domain.model.valueobject;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BirthDateTest {

    @Test
    void shouldGeneratedBirthDate() {
        LocalDate date = LocalDate.of(1980, 6, 22);
        BirthDate birthDate = new BirthDate(date);
        assertThat(birthDate.localDate()).isEqualTo(date);
    }

    @Test
    void shouldBirthDateGeneratedAge() {
        LocalDate date = LocalDate.of(1980, 6, 22);
        BirthDate birthDate = new BirthDate(date);

        assertThat(birthDate.age()).isEqualTo(45);
    }

    @Test
    void shouldNotBirthDateValid() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new BirthDate(LocalDate.of(2099,5,5)));
    }

}