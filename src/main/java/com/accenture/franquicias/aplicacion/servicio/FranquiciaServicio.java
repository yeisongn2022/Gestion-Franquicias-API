package com.accenture.franquicias.aplicacion.servicio;

import com.accenture.franquicias.aplicacion.dto.ProductoMayorStockDTO;
import com.accenture.franquicias.dominio.modelo.*;
import com.accenture.franquicias.dominio.repositorio.RepositorioFranquicia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class FranquiciaServicio {

    private final RepositorioFranquicia repositorioFranquicia;

    // Criterio 2: Agregar una nueva franquicia
    public Mono<Franquicia> crearFranquicia(Franquicia franquicia) {
        if (franquicia.getSucursales() == null) {
            franquicia.setSucursales(new ArrayList<>());
        }
        return repositorioFranquicia.guardar(franquicia);
    }

    // Criterio 3: Agregar una nueva sucursal a una franquicia existente
    public Mono<Franquicia> agregarSucursal(String franquiciaId, Sucursal nuevaSucursal) {
        return repositorioFranquicia.buscarPorId(franquiciaId)
                .flatMap(franquicia -> {
                    if (nuevaSucursal.getProductos() == null) {
                        nuevaSucursal.setProductos(new ArrayList<>());
                    }
                    franquicia.getSucursales().add(nuevaSucursal);
                    return repositorioFranquicia.guardar(franquicia);
                });
    }

    // Criterio 4: Agregar un nuevo producto a una sucursal específica de una franquicia
    public Mono<Franquicia> agregarProducto(String franquiciaId, String sucursalId, Producto nuevoProducto) {
        return repositorioFranquicia.buscarPorId(franquiciaId)
                .flatMap(franquicia -> {
                    franquicia.getSucursales().stream()
                            .filter(s -> s.getId().equals(sucursalId))
                            .findFirst()
                            .ifPresent(s -> s.getProductos().add(nuevoProducto));
                    return repositorioFranquicia.guardar(franquicia);
                });
    }

    // Criterio 5: Eliminar un producto de una sucursal específica
    public Mono<Franquicia> eliminarProducto(String franquiciaId, String sucursalId, String productoId) {
        return repositorioFranquicia.buscarPorId(franquiciaId)
                .flatMap(franquicia -> {
                    franquicia.getSucursales().stream()
                            .filter(s -> s.getId().equals(sucursalId))
                            .findFirst()
                            .ifPresent(s -> s.getProductos().removeIf(p -> p.getId().equals(productoId)));
                    return repositorioFranquicia.guardar(franquicia);
                });
    }

    // Criterio 6: Modificar stock de un producto
    public Mono<Franquicia> actualizarStock(String franquiciaId, String sucursalId, String productoId, Integer nuevoStock) {
        return repositorioFranquicia.buscarPorId(franquiciaId)
                .flatMap(franquicia -> {
                    franquicia.getSucursales().stream()
                            .filter(s -> s.getId().equals(sucursalId))
                            .flatMap(s -> s.getProductos().stream())
                            .filter(p -> p.getId().equals(productoId))
                            .findFirst()
                            .ifPresent(p -> p.actualizarInventario(nuevoStock)); // Usamos el método de validación del dominio
                    return repositorioFranquicia.guardar(franquicia);
                });
    }

    // Criterio 7: mostrar cual es el producto que más stock tiene por sucursal para una franquicia puntual
    public Flux<ProductoMayorStockDTO> obtenerProductosMasStockPorSucursal(String franquiciaId) {
        return repositorioFranquicia.buscarPorId(franquiciaId)
                .flatMapMany(franquicia -> Flux.fromIterable(franquicia.getSucursales()))
                .flatMap(sucursal -> {
                    // Buscamos el producto con más stock dentro de la sucursal
                    return sucursal.getProductos().stream()
                            .max(Comparator.comparingInt(Producto::getInventario))
                            .map(p -> Mono.just(new ProductoMayorStockDTO(
                                    p.getNombre(),
                                    p.getInventario(),
                                    sucursal.getNombre())))
                            .orElse(Mono.empty()); // Si la sucursal no tiene productos, no envía nada
                });
    }

    // Plus: Actualizar el nombre de la franquicia.
    public Mono<Franquicia> actualizarNombre(String id, String nuevoNombre) {
        return repositorioFranquicia.buscarPorId(id)
                .flatMap(franquiciaExistente -> {
                    franquiciaExistente.setNombre(nuevoNombre);
                    return repositorioFranquicia.guardar(franquiciaExistente);
                });
    }

    // Plus: Actualizar nombre de sucursal
    public Mono<Franquicia> actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre) {
        return repositorioFranquicia.buscarPorId(franquiciaId)
                .flatMap(franquicia -> {
                    franquicia.getSucursales().stream()
                            .filter(s -> s.getId().equals(sucursalId))
                            .findFirst()
                            .ifPresent(s -> s.setNombre(nuevoNombre));
                    return repositorioFranquicia.guardar(franquicia);
                });
    }

    // Plus: Actualizar nombre de producto
    public Mono<Franquicia> actualizarNombreProducto(String franquiciaId, String sucursalId, String productoId, String nuevoNombre) {
        return repositorioFranquicia.buscarPorId(franquiciaId)
                .flatMap(franquicia -> {
                    franquicia.getSucursales().stream()
                            .filter(s -> s.getId().equals(sucursalId))
                            .flatMap(s -> s.getProductos().stream())
                            .filter(p -> p.getId().equals(productoId))
                            .findFirst()
                            .ifPresent(p -> p.setNombre(nuevoNombre));
                    return repositorioFranquicia.guardar(franquicia);
                });
    }
}
