package com.example.a1;

public class KeyStore {

    // Store user information (for registration and login)
    public static final String SP_FILENAME = "user-data.dat";
    public static final String USERNAME_KEY = "username";
    public static final String PW_KEY = "password";
    public static final String CONFIRM_PW_KEY = "password-confirmation";

    // Store event category data (for the NewEventCategory class)
    public static final String SP2_FILENAME = "event-category-data.dat";
    public static final String CATEGORY_KEY = "category-id";
    public static final String CATEGORY_NAME_KEY = "category-name";
    public static final String EVENT_COUNT_KEY = "event-count";
    public static final String IS_ACTIVE_KEY = "is-active";

    // Store the event data (for the AddEvent class)
    public static final String SP3_FILENAME = "add-event-data.dat";
    public static final String EVENT_ID_KEY = "event-id";
    public static final String EVENT_NAME_KEY = "event-name";
    public static final String EVENT_CATEGORY_ID_KEY = "event-category-id";
    public static final String TICKETS_AVAILABLE_KEY = "tickets-available";
    public static final String EVENT_IS_ACTIVE = "event-is-active";

}