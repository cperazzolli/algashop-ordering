package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;

import static com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages.ORDER_DOES_NOT_CONTAINS_ITEN;

public class OrderDoesNotContainOrderException extends DomainException{
    
    public OrderDoesNotContainOrderException(String message) {
        super(message);
    }

    public OrderDoesNotContainOrderException(OrderId id, OrderItemId orderItemId) {
        super(String.format(ORDER_DOES_NOT_CONTAINS_ITEN, id, orderItemId));
    }
}
