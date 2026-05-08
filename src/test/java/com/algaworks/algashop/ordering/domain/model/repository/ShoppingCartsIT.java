package com.algaworks.algashop.ordering.domain.model.repository;

import com.algaworks.algashop.ordering.domain.model.entity.*;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disasembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disasembler.ShoppingCartPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.provider.CustomersPersistenceProvider;
import com.algaworks.algashop.ordering.infrastructure.persistence.provider.ShoppingCartsPersistenceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        ShoppingCartsPersistenceProvider.class,
        ShoppingCartPersistenceEntityAssembler.class,
        ShoppingCartPersistenceEntityDisassembler.class,

        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class
})
class ShoppingCartsIT {

    private final ShoppingCarts shoppingCarts;
    private final Customers customers;
    @Autowired
    public ShoppingCartsIT(ShoppingCarts shoppingCarts, Customers customers) {
        this.shoppingCarts = shoppingCarts;
        this.customers = customers;
    }

    @BeforeEach
    void setup() {
        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(
                    CustomerTestDataBuilder.existingCustomer().build()
            );
        }
    }

    @Test
    void shouldUpdateExistentShoppingCart() {
        ShoppingCart cart = ShoppingCartTestDateBuilder.aShoppingCart().anShoppingCart();

        shoppingCarts.add(cart);

        cart = shoppingCarts.ofId(cart.id()).orElseThrow();
        assertThat(cart).isNotNull();

    }
}
