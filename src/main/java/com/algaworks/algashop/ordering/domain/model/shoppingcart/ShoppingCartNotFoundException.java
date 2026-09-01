package com.algaworks.algashop.ordering.domain.model.shoppingcart;


import com.algaworks.algashop.ordering.domain.model.DomainEntityNotFoundException;
import com.algaworks.algashop.ordering.domain.model.ErrorMessages;
import com.algaworks.algashop.ordering.domain.model.customer.ProductId;

public class ShoppingCartNotFoundException extends DomainEntityNotFoundException {

    public ShoppingCartNotFoundException() {
    }

    public ShoppingCartNotFoundException(ProductId productId) {
        super(String.format(ErrorMessages.ERROR_CUSTOMER_NOT_FOUND,productId));
    }
}
