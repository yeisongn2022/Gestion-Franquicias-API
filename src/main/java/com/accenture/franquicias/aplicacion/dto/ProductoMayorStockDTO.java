package com.accenture.franquicias.aplicacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoMayorStockDTO {
    private String nombreProducto;
    private Integer stock;
    private String nombreSucursal;
}
