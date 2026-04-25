package com.accenture.franquicias.infraestructura.adaptador;

import com.accenture.franquicias.dominio.modelo.Franquicia;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioMongoFranquicia extends ReactiveMongoRepository<Franquicia, String> {
}
