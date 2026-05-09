package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.exception.CantAddLoyaltPointsOrderIsNotRead;
import com.algaworks.algashop.ordering.domain.model.exception.OrderNotBeLongsToCustomerException;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;

import java.util.Objects;

public class CustomerLoyatyPointsService {

    private static final LoyaltyPoints basePoints = new LoyaltyPoints(5);
    private static final Money expectedAmountToGivenPoints = new Money("1000");
    public void addPoints(Customer customer, Order order) {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(order);

        if (!customer.id().equals(order.customerId())) {
            throw new OrderNotBeLongsToCustomerException();
        }
        if(!order.isReady()) {
            throw new CantAddLoyaltPointsOrderIsNotRead();
        }

        customer.addLoyaltyPoints(calculatePoints(order));
    }

    private LoyaltyPoints calculatePoints(Order order) {
        if(shouldGivenPointsByAmount(order.totalAmount())){
            Money result = order.totalAmount().divide(expectedAmountToGivenPoints);
            return new LoyaltyPoints(result.value().intValue() * basePoints.value());
        }
        return LoyaltyPoints.ZERO;
    }

    private boolean shouldGivenPointsByAmount(Money amount) {
        return amount.compareTo(expectedAmountToGivenPoints) >= 0;
    }
}
