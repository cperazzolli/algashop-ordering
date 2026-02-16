package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;

import static com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED;

public class OrderCannotBeEditedException extends DomainException{

    public OrderCannotBeEditedException(String message) {
        super(message);
    }

    public static OrderCannotBeEditedException ifChangeable(OrderId id, OrderStatus status) {
        throw new OrderCannotBeEditedException(String.format(ERROR_ORDER_CANNOT_BE_EDITED,id,status));
    }
}
