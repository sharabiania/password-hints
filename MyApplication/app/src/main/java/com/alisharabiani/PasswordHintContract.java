package com.alisharabiani;

import android.provider.BaseColumns;

public final class PasswordHintContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public PasswordHintContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_ACCOUNT = "account";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORDHINT = "passwordhint";

    }
}