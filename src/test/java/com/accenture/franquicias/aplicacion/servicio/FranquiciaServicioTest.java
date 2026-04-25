package com.accenture.franquicias.aplicacion.servicio;

import com.accenture.franquicias.aplicacion.dto.ProductoMayorStockDTO;
import com.accenture.franquicias.dominio.modelo.Franquicia;
import com.accenture.franquicias.dominio.modelo.Producto;
import com.accenture.franquicias.dominio.modelo.Sucursal;
import com.accenture.franquicias.dominio.repositorio.RepositorioFranquicia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FranquiciaServicioTest {

    private RepositorioFranquicia repositorio;
    private FranquiciaServicio servicio;

    @BeforeEach
    void iniciar() {
        repositorio = Mockito.mock(RepositorioFranquicia.class);
        servicio = new FranquiciaServicio(repositorio);
    }

    @Test
    @DisplayName("Debe crear una franquicia y persistirla")
    void crearFranquiciaTest() {
        Franquicia franquicia = new Franquicia("1", "Franquicia Test", new ArrayList<>());
        when(repositorio.guardar(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

        StepVerifier.create(servicio.crearFranquicia(franquicia))
                .expectNextMatches(f -> f.getNombre().equals("Franquicia Test"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe agregar una sucursal a una franquicia existente")
    void agregarSucursalTest() {
        Franquicia franquicia = new Franquicia("1", "Franquicia Original", new ArrayList<>());
        Sucursal sucursal = new Sucursal("1", "Sucursal Medellin", new ArrayList<>());

        when(repositorio.buscarPorId("1")).thenReturn(Mono.just(franquicia));
        when(repositorio.guardar(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

        StepVerifier.create(servicio.agregarSucursal("1", sucursal))
                .expectNextMatches(f -> f.getSucursales().size() == 1 &&
                        f.getSucursales().get(0).getNombre().equals("Sucursal Medellin"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe fallar al intentar actualizar inventario con valor negativo")
    void validarInventarioNegativoTest() {
        Producto producto = new Producto("1", "Producto Falla", 10);

        // Verificamos que el método del dominio lance la excepción esperada
        StepVerifier.create(Mono.fromRunnable(() -> producto.actualizarInventario(-1)))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe encontrar el producto con más stock por sucursal")
    void obtenerProductoMasStockTest() {
        // Mock de datos complejo
        Producto p1 = new Producto("1", "Arroz", 10);
        Producto p2 = new Producto("2", "Frijol", 100); // El mayor de s1
        Sucursal s1 = new Sucursal("1", "Sucursal Norte", new ArrayList<>(List.of(p1, p2)));

        Producto p3 = new Producto("3", "Azúcar", 500); // El mayor de s2
        Sucursal s2 = new Sucursal("2", "Sucursal Sur", new ArrayList<>(List.of(p3)));

        Franquicia franquicia = new Franquicia("1", "Gran Tienda", new ArrayList<>(List.of(s1, s2)));

        when(repositorio.buscarPorId("1")).thenReturn(Mono.just(franquicia));

        // Verificamos que el Flux emita dos DTOs con los productos máximos
        StepVerifier.create(servicio.obtenerProductosMasStockPorSucursal("1"))
                .expectNextMatches(dto -> dto.getNombreProducto().equals("Frijol") &&
                        dto.getNombreSucursal().equals("Sucursal Norte"))
                .expectNextMatches(dto -> dto.getNombreProducto().equals("Azúcar") &&
                        dto.getNombreSucursal().equals("Sucursal Sur"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe eliminar un producto de una sucursal correctamente")
    void eliminarProductoTest() {
        Producto p1 = new Producto("1", "Eliminar", 10);
        Sucursal s1 = new Sucursal("1", "Sucursal", new ArrayList<>(List.of(p1)));
        Franquicia f1 = new Franquicia("1", "Franquicia", new ArrayList<>(List.of(s1)));

        when(repositorio.buscarPorId("1")).thenReturn(Mono.just(f1));
        when(repositorio.guardar(any(Franquicia.class))).thenReturn(Mono.just(f1));

        StepVerifier.create(servicio.eliminarProducto("1", "1", "1"))
                .expectNextMatches(f -> f.getSucursales().get(0).getProductos().isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar Mono.empty() si la franquicia no existe al buscar stock máximo")
    void obtenerProductoMasStockFranquiciaNoExisteTest() {
        // Simulamos que el repositorio no encuentra la franquicia
        when(repositorio.buscarPorId("999")).thenReturn(Mono.empty());

        StepVerifier.create(servicio.obtenerProductosMasStockPorSucursal("999"))
                .expectNextCount(0) // No emite nada
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe actualizar el nombre de la franquicia correctamente")
    void actualizarNombreFranquiciaTest() {
        Franquicia franquicia = new Franquicia("1", "Nombre Viejo", new ArrayList<>());
        when(repositorio.buscarPorId("1")).thenReturn(Mono.just(franquicia));
        when(repositorio.guardar(any(Franquicia.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(servicio.actualizarNombre("1", "Nombre Nuevo"))
                .expectNextMatches(f -> f.getNombre().equals("Nombre Nuevo"))
                .verifyComplete();
    }
}
