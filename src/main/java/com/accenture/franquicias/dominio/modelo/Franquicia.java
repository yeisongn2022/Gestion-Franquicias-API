package com.accenture.franquicias.dominio.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Franquicia {
    private String id;
    private String nombre;
    private List<Sucursal> sucursales;
}
