package com.vspcom.cotizador20.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBConexion extends SQLiteOpenHelper {

    private static final String DB_NAME = "vspCom23.db";
    private static final int DB_VERSION = 1;

    public DBConexion(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name = '"
                + DBManager.TABLA_PRODUCTOS + "'", null);

        if (cursor != null && cursor.moveToFirst() && cursor.getInt(0) == 0) {
            sqLiteDatabase.execSQL(DBManager.TABLA_PRODUCTOS_CREATE);
            sqLiteDatabase.execSQL(DBManager.TABLA_COTIZAPART_CREATE);
            sqLiteDatabase.execSQL(DBManager.TABLA_COTIZACION_CREATE);
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_PRODUCTOS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_COTIZACION);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_COTIZAPART);

            onCreate(sqLiteDatabase);
        }
    }
}
