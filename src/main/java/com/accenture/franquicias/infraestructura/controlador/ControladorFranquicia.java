package com.accenture.franquicias.infraestructura.controlador;

import com.accenture.franquicias.aplicacion.dto.ProductoMayorStockDTO;
import com.accenture.franquicias.aplicacion.servicio.FranquiciaServicio;
import com.accenture.franquicias.dominio.modelo.Franquicia;
import com.accenture.franquicias.dominio.modelo.Producto;
import com.accenture.franquicias.dominio.modelo.Sucursal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias")
@RequiredArgsConstructor
public class ControladorFranquicia {

    private final FranquiciaServicio servicio;

    @PostMapping
    public Mono<Franquicia> crear(@RequestBody Franquicia franquicia) {
        return servicio.crearFranquicia(franquicia);
    }

    @PostMapping("/{id}/sucursales")
    public Mono<Franquicia> agregarSucursal(@PathVariable String id, @RequestBody Sucursal sucursal) {
        return servicio.agregarSucursal(id, sucursal);
    }

    @PostMapping("/{franquiciaId}/sucursales/{sucursalId}/productos")
    public Mono<Franquicia> agregarProducto(@PathVariable String franquiciaId,
                                            @PathVariable String sucursalId,
                                            @RequestBody Producto producto) {
        return servicio.agregarProducto(franquiciaId, sucursalId, producto);
    }

    @DeleteMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}")
    public Mono<Franquicia> eliminarProducto(@PathVariable String franquiciaId,
                                             @PathVariable String sucursalId,
                                             @PathVariable String productoId) {
        return servicio.eliminarProducto(franquiciaId, sucursalId, productoId);
    }

    @PatchMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock")
    public Mono<Franquicia> actualizarStock(@PathVariable String franquiciaId,
                                            @PathVariable String sucursalId,
                                            @PathVariable String productoId,
                                            @RequestParam Integer cantidad) {
        return servicio.actualizarStock(franquiciaId, sucursalId, productoId, cantidad);
    }

    @GetMapping("/{id}/productos-max-stock")
    public Flux<ProductoMayorStockDTO> obtenerMaximos(@PathVariable String id) {
        return servicio.obtenerProductosMasStockPorSucursal(id);
    }

    @PatchMapping("/{id}/nombre")
    public Mono<Franquicia> actualizarNombre(@PathVariable String id, @RequestParam String nuevoNombre) {
        return servicio.actualizarNombre(id, nuevoNombre);
    }

    @PatchMapping("/{franquiciaId}/sucursales/{sucursalId}/nombre")
    public Mono<Franquicia> actualizarNombreSucursal(@PathVariable String franquiciaId,
                                                     @PathVariable String sucursalId,
                                                     @RequestParam String nuevoNombre) {
        return servicio.actualizarNombreSucursal(franquiciaId, sucursalId, nuevoNombre);
    }

    @PatchMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/nombre")
    public Mono<Franquicia> actualizarNombreProducto(@PathVariable String franquiciaId,
                                                     @PathVariable String sucursalId,
                                                     @PathVariable String productoId,
                                                     @RequestParam String nuevoNombre) {
        return servicio.actualizarNombreProducto(franquiciaId, sucursalId, productoId, nuevoNombre);
    }
}
