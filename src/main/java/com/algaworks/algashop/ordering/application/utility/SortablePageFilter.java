package com.algaworks.algashop.ordering.application.utility;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;

import javax.swing.*;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class SortablePageFilter<T> extends PageFilter{
    private T sortedByProperty;
    private Sort.Direction direction;

    public abstract T getSortByPropertyOrDefault();
    public abstract Sort.Direction getSortDirectionOrDefault();
}
