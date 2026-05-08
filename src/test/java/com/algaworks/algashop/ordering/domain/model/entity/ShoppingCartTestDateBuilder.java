package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;

import static com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

public class ShoppingCartTestDateBuilder {

    private CustomerId custumer = DEFAULT_CUSTOMER_ID;
    private boolean withItems = true;

    private ShoppingCartTestDateBuilder() {}

    public static ShoppingCartTestDateBuilder aShoppingCart() {
        return new ShoppingCartTestDateBuilder();
    }

    public ShoppingCart anShoppingCart() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(custumer);
        if (withItems) {
            shoppingCart.addItem(ProductTestDataBuilder.aProduct().build(),
                    new Quantity(2)
            );

            shoppingCart.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(),
                    new Quantity(1)
            );
        }
        return shoppingCart;
    }
}
