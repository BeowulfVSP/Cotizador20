package com.vspcom.cotizador20.DB;

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

    public static final String TABLA_PRODUCTOS_CREATE = "create table productos(_codigo text not null,  descripcion text not null, linea text not null, marca text not null, precio text not null, impuesto text not null, ubicacion text not null, fabricacion text not null))";

    private DBConexion _conexion;
    //private SQLiteDatabase

}
