package edu.eci.arsw.blueprints;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
    info = @Info(
        title = "API de Blueprints",
        version = "1.0",
        description = "API para la gestión de blueprints. Permite crear, consultar y modificar blueprints de manera centralizada."
    )
)
@SpringBootApplication
public class BlueprintsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlueprintsApplication.class, args);
    }
}
