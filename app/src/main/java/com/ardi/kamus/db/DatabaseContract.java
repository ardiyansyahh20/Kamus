package com.ardi.kamus.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    public static String TABLE_ID_TO_EN = "id_to_en";
    public static String TABLE_EN_TO_ID = "en_to_id";

    public static final class KamusColumns implements BaseColumns {
        public static String WORD = "word";
        public static String DESC = "description";
    }
}
