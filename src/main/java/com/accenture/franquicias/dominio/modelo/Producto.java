package com.accenture.franquicias.dominio.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    private String id;
    private String nombre;
    private Integer inventario;

    public void actualizarInventario(Integer nuevoInventario) {
        if (nuevoInventario == null || nuevoInventario < 0) {
            throw new IllegalArgumentException("El inventario del producto debe ser mayor a cero");
        }
        this.inventario = nuevoInventario;
    }
}
