package com.accenture.franquicias.infraestructura.adaptador;

import com.accenture.franquicias.dominio.modelo.Franquicia;
import com.accenture.franquicias.dominio.repositorio.RepositorioFranquicia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AdaptadorFranquicia implements RepositorioFranquicia {

    private final RepositorioMongoFranquicia repositorio;

    @Override
    public Mono<Franquicia> guardar(Franquicia franquicia) {
        return repositorio.save(franquicia);
    }

    @Override
    public Mono<Franquicia> buscarPorId(String id) {
        return repositorio.findById(id);
    }
}
