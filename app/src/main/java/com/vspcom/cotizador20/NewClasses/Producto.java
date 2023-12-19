package com.vspcom.cotizador20.NewClasses;

public class Producto {
    private String articulo;
    private String descripcion;
    private String linea;
    private String marca;
    private String precio;
    private String impuesto;
    private String ubicacion;
    private String fabricante;

    public Producto(String articulo, String descripcion, String linea, String marca, String precio, String impuesto, String ubicacion, String fabricante) {
        this.articulo = articulo;
        this.descripcion = descripcion;
        this.linea = linea;
        this.marca = marca;
        this.precio = precio;
        this.impuesto = impuesto;
        this.ubicacion = ubicacion;
        this.fabricante = fabricante;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }
}
