package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ShoppingCartTest {

    @Test
    void shouldGenerateShoppingCart() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Assertions.assertWith(shoppingCart,
                o -> Assertions.assertThat(o.id()).isNotNull(),
                o -> Assertions.assertThat(o.customerId()).isEqualTo(customerId),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(Money.ZERO),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(Quantity.ZERO),
                o -> Assertions.assertThat(o.items()).isEmpty()
        );
    }

    @Test
    void shouldAddItem() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();
        ProductId productId = product.id();

        shoppingCart.addItem(product, new Quantity(1));

        Assertions.assertThat(shoppingCart.items().size()).isEqualTo(1);

        ShoppingCartItem shoppingCartItem = shoppingCart.items().iterator().next();

        Assertions.assertWith(shoppingCartItem,
                (i) -> Assertions.assertThat(i.id()).isNotNull(),
                (i) -> Assertions.assertThat(i.name()).isEqualTo(new ProductName("Mouse Pad")),
                (i) -> Assertions.assertThat(i.productId()).isEqualTo(productId),
                (i) -> Assertions.assertThat(i.price()).isEqualTo(new Money("100")),
                (i) -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(1))
        );
    }
}
