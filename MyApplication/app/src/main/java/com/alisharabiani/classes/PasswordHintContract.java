package com.alisharabiani.classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class PasswordHintContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public PasswordHintContract() {}

    /* Inner class that defines the table contents */
    public static abstract class HintEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
      //  public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_ACCOUNT = "account";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORDHINT = "passwordhint";

    }



}

