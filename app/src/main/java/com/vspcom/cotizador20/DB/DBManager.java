package com.vspcom.cotizador20.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.vspcom.cotizador20.NewClasses.Producto;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

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

    public static final String TABLA_COTIZACION           = "cotizaciones";
    public static final String COTIZACION_ID              = "_id";
    public static final String COTIZACION_CLIENTE         = "cliente";
    public static final String COTIZACION_TELEFONO        = "telefono";
    public static final String COTIZACION_USUARIO         = "usuario";
    public static final String COTIZACION_ACEPTADA        = "aceptada";
    public static final String COTIZACION_COMENTARIOS     = "comentarios";
    public static final String COTIZACION_FECHA           = "fecha";
    public static final String COTIZACION_HORA            = "hora";
    public static final String COTIZACION_NUMERO          = "numero";
    public static final String COTIZACION_VALIDA          = "valida";
    public static final String COTIZACION_LISTA_EQUIPOS   = "listaEquipos";
    public static final String COTIZACION_LISTA_PROGRAMAS = "listaProgramas";
    public static final String COTIZACION_LISTA_SERVICIOS = "listaServicios";
    public static final String COTIZACION_VENDEDOR        = "vendedor";
    public static final String COTIZACION_TITULO_UNO      = "tituloUno";
    public static final String COTIZACION_TITULO_DOS      = "tituloDos";
    public static final String COTIZACION_TITULO_TRES     = "tituloTres";
    public static final String COTIZACION_SERIE           = "serie";
    public static final String COTIZACION_NSERVICIO       = "nservicio";
    public static final String COTIZACION_PEDIDO          = "pedido";
    public static final String COTIZACION_NOTAS           = "notas";
    public static final String COTIZACION_NOTAS2          = "notas2";
    public static final String COTIZACION_NOTAS3          = "notas3";
    public static final String COTIZACION_LEYENDA         = "leyenda";
    public static final String COTIZACION_BGIMG           = "bgImg";
    public static final String COTIZACION_CAPTURO         = "capturo";
    public static final String COTIZACION_SOLO_VENTA      = "soloVenta";

    public static final String TABLA_COTIZACION_CREATE = "create table " + TABLA_COTIZACION + "(" +
            COTIZACION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COTIZACION_CLIENTE + " text not null, " +
            COTIZACION_TELEFONO + " text not null, " +
            COTIZACION_USUARIO + " text not null, " +
            COTIZACION_ACEPTADA + " text not null, " +
            COTIZACION_COMENTARIOS + " text not null, " +
            COTIZACION_FECHA + " text not null, " +
            COTIZACION_HORA + " text not null, " +
            COTIZACION_NUMERO + " text not null, " +
            COTIZACION_VALIDA + " text not null, " +
            COTIZACION_LISTA_EQUIPOS + " text not null, " +
            COTIZACION_LISTA_PROGRAMAS + " text not null, " +
            COTIZACION_LISTA_SERVICIOS + " text not null, " +
            COTIZACION_VENDEDOR + " text not null, " +
            COTIZACION_TITULO_UNO + " text not null, " +
            COTIZACION_TITULO_DOS + " text not null, " +
            COTIZACION_TITULO_TRES + " text not null, " +
            COTIZACION_SERIE + " text not null, " +
            COTIZACION_NSERVICIO + " text not null, " +
            COTIZACION_PEDIDO + " text not null, " +
            COTIZACION_NOTAS + " text not null, " +
            COTIZACION_NOTAS2 + " text not null, " +
            COTIZACION_NOTAS3 + " text not null, " +
            COTIZACION_LEYENDA + " text not null, " +
            COTIZACION_BGIMG + " text not null, " +
            COTIZACION_CAPTURO + " text not null, " +
            COTIZACION_SOLO_VENTA + " text not null)";

    public static final String TABLA_COTIZAPART       = "cotizapart";
    public static final String COTIZAPART_ID          = "_id";
    public static final String COTIZAPART_COTIZACION  = "cotizacion";
    public static final String COTIZAPART_ARTICULO    = "articulo";
    public static final String COTIZAPART_DESCRIPCION = "descripcion";
    public static final String COTIZAPART_SECCION     = "seccion";
    public static final String COTIZAPART_CANTIDAD    = "cantidad";
    public static final String COTIZAPART_UNITARIO    = "unitario";
    public static final String COTIZAPART_IMPORTE     = "importe";
    public static final String COTIZAPART_DESCUENTO   = "descuento";
    public static final String COTIZAPART_CODIGO      = "codigo";
    public static final String COTIZAPART_SERIE       = "serie";

    public static final String TABLA_COTIZAPART_CREATE = "create table " + TABLA_COTIZAPART + "(" +
            COTIZAPART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COTIZAPART_COTIZACION + " text not null, " +
            COTIZAPART_ARTICULO + " text not null, " +
            COTIZAPART_DESCRIPCION + " text not null, " +
            COTIZAPART_SECCION + " text not null, " +
            COTIZAPART_CANTIDAD + " text not null, " +
            COTIZAPART_UNITARIO + " text not null, " +
            COTIZAPART_IMPORTE + " text not null, " +
            COTIZAPART_DESCUENTO + " text not null, " +
            COTIZAPART_CODIGO + " text not null, " +
            COTIZAPART_SERIE + " text not null)";

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

    public void createDatabase() {
        _basededatos.execSQL(TABLA_PRODUCTOS_CREATE);
        _basededatos.execSQL(TABLA_COTIZAPART_CREATE);
        _basededatos.execSQL(TABLA_COTIZACION_CREATE);
    }

    public void insertPorducts(Producto producto) {
        ContentValues values = new ContentValues();
        values.put(PRODUCTO_CODIGO, producto.getArticulo());
        values.put(PRODUCTO_DESCRIPCION, producto.getDescripcion());
        values.put(PRODUCTO_LINEA, producto.getLinea());
        values.put(PRODUCTO_MARCA, producto.getMarca());
        values.put(PRODUCTO_PRECIO, producto.getPrecio());
        values.put(PRODUCTO_IMPUESTO, producto.getImpuesto());
        values.put(PRODUCTO_UBICACION, producto.getUbicacion());
        values.put(PRODUCTO_FABRICANTE, producto.getFabricante());

        _basededatos.insertWithOnConflict(
                TABLA_PRODUCTOS,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<Producto> getAllProducts() {
        List<Producto> productos = new ArrayList<>();

        String[] columns = {
                PRODUCTO_CODIGO,
                PRODUCTO_DESCRIPCION,
                PRODUCTO_LINEA,
                PRODUCTO_MARCA,
                PRODUCTO_PRECIO,
                PRODUCTO_IMPUESTO,
                PRODUCTO_UBICACION,
                PRODUCTO_FABRICANTE
        };

        Cursor cursor = _basededatos.query(
                TABLA_PRODUCTOS,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            int codigoIndex = cursor.getColumnIndex(PRODUCTO_CODIGO);
            int descripcionIndex = cursor.getColumnIndex(PRODUCTO_DESCRIPCION);
            int lineaIndex = cursor.getColumnIndex(PRODUCTO_LINEA);
            int marcaIndex = cursor.getColumnIndex(PRODUCTO_MARCA);
            int precioIndex = cursor.getColumnIndex(PRODUCTO_PRECIO);
            int impuestoIndex = cursor.getColumnIndex(PRODUCTO_IMPUESTO);
            int ubicacionIndex = cursor.getColumnIndex(PRODUCTO_UBICACION);
            int fabricanteIndex = cursor.getColumnIndex(PRODUCTO_FABRICANTE);

            while (cursor.moveToNext()) {
                if (codigoIndex != -1 && descripcionIndex != -1 && lineaIndex != -1 &&
                        marcaIndex != -1 && precioIndex != -1 && impuestoIndex != -1 &&
                        ubicacionIndex != -1 && fabricanteIndex != -1) {

                    String codigo = cursor.getString(codigoIndex);
                    String descripcion = cursor.getString(descripcionIndex);
                    String linea = cursor.getString(lineaIndex);
                    String marca = cursor.getString(marcaIndex);
                    String precio = cursor.getString(precioIndex);
                    String impuesto = cursor.getString(impuestoIndex);
                    String ubicacion = cursor.getString(ubicacionIndex);
                    String fabricante = cursor.getString(fabricanteIndex);

                    Producto producto = new Producto(codigo, descripcion, linea, marca, precio, impuesto, ubicacion, fabricante);
                    productos.add(producto);
                }
            }
            cursor.close();
        }

        return productos;
    }

}
