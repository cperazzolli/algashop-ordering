package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;

import static com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages.ERROR_DELIVERY_DATE_CONNOT_BE_IN_THE_PAST;

public class OrderShippingDeliveryDateException extends DomainException{


    public OrderShippingDeliveryDateException(OrderId id) {
        super(String.format(ERROR_DELIVERY_DATE_CONNOT_BE_IN_THE_PAST,id));
    }
}
