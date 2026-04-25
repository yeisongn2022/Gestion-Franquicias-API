package com.accenture.franquicias.dominio.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sucursal {
    private String id;
    private String nombre;
    private List<Producto> productos;
}
