package com.algaworks.algashop.ordering.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;


class PhoneTest {

    @Test
    void shouldGeneratedDocument() {
        Phone phone = new Phone("00-0000-0000");
        assertThat(phone).isEqualTo(new Phone("00-0000-0000"));
    }

    @Test
    void shouldNotGeneratedDocument() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy( () -> new Phone(""));
    }

}