package com.commonlibrary.common_library.common.enums;

public class KafkaTopics {
    private KafkaTopics(){}

    public static final String HOTEL_CREATED = "hotel-created";
    public static final String BOOKING_CREATED = "booking-created";
    public static final String BOOKING_CANCELLED = "booking-cancelled";
    public static final String PAYMENT_REQUEST = "payment-request";
    public static final String PAYMENT_SUCCESS = "payment-success";
    public static final String PAYMENT_FAILED = "payment-failed";
    public static final String USER_REGISTERED = "user-registered";
    public static final String INVENTORY_RESERVED = "inventory-reserved";
    public static final String INVENTORY_RELEASED = "inventory-released";
    public static final String NOTIFICATION_EMAIL = "notification-email";
    public static final String DEAD_LETTER = "dead-letter";
    public static final String USER_LOGIN ="user-login";
}
