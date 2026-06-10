package com.algaworks.algashop.ordering.application.order.query;

import com.algaworks.algashop.ordering.application.utility.SortablePageFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderFilter extends SortablePageFilter<OrderFilter.SortType> {

    private String status;
    private String orderId;
    private UUID customerId;
    private OffsetDateTime placedAtFrom;
    private OffsetDateTime placedAtTo;
    private BigDecimal totalAmountFrom;
    private BigDecimal totalAmountTo;

    @Override
    public SortType getSortByPropertyOrDefault() {
        return getSortedByProperty() == null ? SortType.PLACED_AT : getSortedByProperty();
    }

    @Override
    public Sort.Direction getSortDirectionOrDefault() {
        return getDirection() == null ? Sort.Direction.ASC : getDirection();
    }

    @Getter
    @RequiredArgsConstructor
    public enum SortType {
        PLACED_AT("placedAt"),
        PAID_AT("paidAt"),
        CANCELD_AT("canceledAt"),
        READY_AT("readyAt"),
        STATUS("status");
        private final String propertyName;
    }


}
