package com.accenture.franquicias.infraestructura.configuracion;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
@EnableReactiveMongoAuditing
public class ConfiguracionMongo {

    // Leemos la variable MongoDB-URI desde el application.properties
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongoUri))
                .serverApi(serverApi)
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        // Extraemos el nombre de la base de datos de la URI (gestion_franquicia_db)
        String baseDeDatosDefecto = connectionString.getDatabase();
        return new ReactiveMongoTemplate(mongoClient(), baseDeDatosDefecto);
    }
}
