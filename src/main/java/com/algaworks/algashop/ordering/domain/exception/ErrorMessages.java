package com.algaworks.algashop.ordering.domain.exception;

public class ErrorMessages {

    public static final String VALIDATION_ERROR_EMAIL_IS_INVALID = "E-mail is invalid.";
    public static final String VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST = "BirthDate must be a past date";

    public static final String VALIDATION_ERROR_FULLNAME_IS_NULL = "FullName cannot be null";
    public static final String VALIDATION_ERROR_FULLNAME_IS_BLANK = "FullName cannot be blank";

    public static final String ERROR_CUSTOMER_ARCHIVED = "Customer is archived cannot be changed";

    public static final String ERROR_STATUS_CANNOT_BE_CHANGED = "Cannnot be order %s status from %s to %s";

    public static final String ERROR_DELIVERY_DATE_CONNOT_BE_IN_THE_PAST = "Order %s cannot be delivery in the past";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_ITEMS = "Order %s cannot be placed, it has no items";
    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_SHIPPING_INFO = "Order %s cannot be placed, it has no shipping info";
    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_BILLING_INFO = "Order %s cannot be placed, it has no billing info";
    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_VALID_SHIPPING_COST = "Order %s cannot be placed, it has no valid shipping cost";
    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_VALID_DELIVERY_DATE = "Order %s cannot be placed, it has no valid delivery date";
    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_PAYMENT_METHOD = "Order %s cannot be placed, it has no payment method";

    public static final String ORDER_DOES_NOT_CONTAINS_ITEN = "Order %s does not contains item %s";


}
