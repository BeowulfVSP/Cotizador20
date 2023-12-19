package com.vspcom.cotizador20.NewClasses;

public class CSpinner {
    private String id;
    private String descripcion;
    private String precio;

    public CSpinner(String id, String descripcion, String precio) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrecio() {
        return precio;
    }
}
