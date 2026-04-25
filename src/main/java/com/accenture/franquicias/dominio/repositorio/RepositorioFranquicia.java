package com.accenture.franquicias.dominio.repositorio;

import com.accenture.franquicias.dominio.modelo.Franquicia;
import reactor.core.publisher.Mono;

public interface RepositorioFranquicia {
    Mono<Franquicia> guardar(Franquicia franquicia);
    Mono<Franquicia> buscarPorId(String id);
}
