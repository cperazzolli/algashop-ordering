package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;

import static com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages.ERROR_STATUS_CANNOT_BE_CHANGED;

public class OrderStatusCannotBeChangedExcetpion extends DomainException{

    public OrderStatusCannotBeChangedExcetpion(OrderId id, OrderStatus status, OrderStatus newStatus) {
        super(String.format(ERROR_STATUS_CANNOT_BE_CHANGED, id,status,newStatus));
    }
}
