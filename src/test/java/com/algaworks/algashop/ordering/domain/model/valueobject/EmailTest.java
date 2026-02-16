package com.algaworks.algashop.ordering.domain.model.valueobject;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class EmailTest {

    @Test
    void shouldGeneratedEmail() {
        Email email = new Email("jhon.doe@email.com");
        assertThat(email).isEqualTo(new Email("jhon.doe@email.com"));
    }

    @Test
    void shouldNotGeneratedDocument() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy( () -> new Email(""));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy( () -> new Email("john"));
    }

}