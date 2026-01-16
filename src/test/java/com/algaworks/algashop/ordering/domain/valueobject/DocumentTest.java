package com.algaworks.algashop.ordering.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class DocumentTest {


    @Test
    void shouldGeneratedDocument() {
        Document document = new Document("0000-0000-00");
        assertThat(document).isEqualTo(new Document("0000-0000-00"));
    }

    @Test
    void shouldNotGeneratedDocument() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy( () -> new Document(""));
    }

}