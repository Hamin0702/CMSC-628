package com.example.a0412classactivity;

import android.provider.BaseColumns;

public class DatabaseSchema {
    public DatabaseSchema(){

    }

    public static final class TableDB implements BaseColumns {
        public static final String TABLE_NAME = "Username";
        public static final String COLUMN_FIRSTNAME = "Firstname";
        public static final String COLUMN_LASTNAME = "Lastname";
    }
}
