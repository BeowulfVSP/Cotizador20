package com.vspcom.cotizador20.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    // TABLA PRODUCTOS
    public static final String TABLA_PRODUCTOS        = "productos";
    public static final String PRODUCTO_CODIGO        = "_codigo";
    public static final String PRODUCTO_DESCRIPCION   = "descripcion";
    public static final String PRODUCTO_LINEA         = "linea";
    public static final String PRODUCTO_MARCA         = "marca";
    public static final String PRODUCTO_PRECIO        = "precio";
    public static final String PRODUCTO_IMPUESTO      = "impuesto";
    public static final String PRODUCTO_UBICACION     = "ubicacion";
    public static final String PRODUCTO_FABRICANTE    = "fabricante";

    public static final String TABLA_PRODUCTOS_CREATE = "create table " + TABLA_PRODUCTOS + "(" +
            PRODUCTO_CODIGO + " text not null, " +
            PRODUCTO_DESCRIPCION + " text not null, " +
            PRODUCTO_LINEA + " text not null, " +
            PRODUCTO_MARCA + " text not null, " +
            PRODUCTO_PRECIO + " text not null, " +
            PRODUCTO_IMPUESTO + " text not null, " +
            PRODUCTO_UBICACION + " text not null, " +
            PRODUCTO_FABRICANTE + " text not null)";

    private DBConexion _conexion;
    private SQLiteDatabase _basededatos;

    public DBManager(Context context) {
        _conexion = new DBConexion(context);
    }

    public DBManager open() throws SQLException {
        _basededatos = _conexion.getWritableDatabase();
        return this;
    }

    public void close() {
        _conexion.close();
    }

}
