package com.vspcom.cotizador20.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBConexion extends SQLiteOpenHelper {

    private static final String DB_NAME = "vspComputer23.db";
    private static final int DB_VERSION = 1;

    public DBConexion(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBManager.TABLA_PRODUCTOS_CREATE);
        sqLiteDatabase.execSQL(DBManager.TABLA_COTIZAPART_CREATE);
        sqLiteDatabase.execSQL(DBManager.TABLA_COTIZACION_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_PRODUCTOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_COTIZACION);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_COTIZAPART);
    }
}
