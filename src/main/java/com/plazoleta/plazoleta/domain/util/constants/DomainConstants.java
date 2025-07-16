package com.plazoleta.plazoleta.domain.util.constants;

public class DomainConstants {
    public static final String WRONG_ARGUMENT_NIT_MESSAGE = "NIT can only contain numbers";
    public static final String MAX_SIZE_EXCEEDED_PHONE_NUMBER = "Phone number can only contain 13 characters";
    public static final String WRONG_ARGUMENT_PHONE_MESSAGE = "Phone number must begin with + followed by 12 numbers";
    public static final String WRONG_ARGUMENT_NAME = "Name can't only contain numbers";
    public static final String NAME_EMPTY_MESSAGE = "Name field can't be empty";
    public static final String NAME_NULL_MESSAGE = "Name field can't be null";
    public static final String NIT_EMPTY_MESSAGE = "NIT field can't be empty";
    public static final String NIT_NULL_MESSAGE = "NIT field can't be null";
    public static final String ADDRESS_EMPTY_MESSAGE = "Address field can't be empty";
    public static final String ADDRESS_NULL_MESSAGE = "Address field can't be null";
    public static final String PHONENUMBER_EMPTY_MESSAGE = "PhoneNumber field can't be empty";
    public static final String PHONENUMBER_NULL_MESSAGE = "PhoneNumber field can't be null";
    public static final String LOGOURL_EMPTY_MESSAGE = "LogoUrl field can't be empty";
    public static final String PAGE_NUMBER_INVALID = "Page number cannot be less than 0";
    public static final String PAGE_SIZE_INVALID = "Page size cannot be less than 0";
    public static final String LOGOURL_NULL_MESSAGE = "LogoUrl field can't be null";
    public static final String OWNERID_NULL_MESSAGE = "OwnerId field can't be null";
    public static final String WRONG_OWNER_ID = "Owner ID must be a positive number";
    public static final String WRONG_ARGUMENT_PRICE = "Price must be a positive integer greater than 0.";
    public static final String WRONG_ARGUMENT_DESCRIPTION = "Description can't be null or empty";
    public static final String WRONG_ARGUMENT_URLIMAGE = "URL image can't be null or empty";
    public static final String WRONG_ARGUMENT_CATEGORY = "Category can't be null or empty";
    public static final String WRONG_ARGUMENT_RESTAURANT = "Restaurant can't be null or empty";
    public static final String CHANGE_DISH_NOT_ALLOWED = "You are not allowed to update this dish.";
    public static final String CREATE_DISH_NOT_ALLOWED = "You are not allowed to create this dish.";
    public static final String UPDATE_DISH_NOT_ALLOWED = "You are not allowed to update this dish.";
    public static final String DISH_NOT_FOUND = "Dish was not foud with ID: ";
    public static final String ORDER_ALREADY_CREATED = "You already have an order created";
    public static final String ID_CATEGORY_MISSING = "Category ID is missing";
    public static final String NON_EXISTING_RESTAURANT = "Restaurant does not exist";
    public static final String NON_EXISTING_DISH = "Dish does not exist";
    public static final String NOT_ALLOWED_TO_CHECK_OTHER_RESTAURANTS_ORDERS = "You are not allowed to check " +
            "the orders for restaurants where you don't work";
    public static final String OWNER_ID = "ownerId";
    public static final String ORDER_NOT_FOUND = "Order not found";
    public static final String ORDER_NOT_PENDING = "Order must be in pending to be assigned";
    public static final String EMPLOYEE_ID_MANDATORY = "Employee Id can't be null";
    public static final String NOT_ALLOWED_TO_ASSIGN_ORDERS = "You can't assign orders to that employee";
    public static final String EMPLOYEE_ID = "employeeId";
    public static final String CLIENT_ID = "clientId";
    public static final String ORDER_STATUS_MANDATORY = "Order status can't be null";
    public static final String NOT_ALLOWED_TO_EDIT_ORDERS = "You can't edit this order";
    public static final String ORDER_NOT_PREPARATION = "Order must be in preparation to be changed";
    public static final String ORDER_STATUS_INVALID = "Order Status can be only changed to Listo";
    public static final String ORDER_ALREADY_COMPLETED = "Order is already completed";
    public static final String ORDER_NOT_READY = "Order is not ready";
    public static final String ORDER_CANCELED = "Order is canceled";
    public static final String INVALID_SECURITY_PIN = "INVALID SECURITY PIN";
    public static final String ORDER_PIN_MANDATORY = "Security pin is mandatory";
    public static final String ORDER_STATUS_NO_VALID = "Your order is being prepared or ready and can't be canceled now";
    public static final String ORDER_CANT_NULL = "Order can't be null";


}
