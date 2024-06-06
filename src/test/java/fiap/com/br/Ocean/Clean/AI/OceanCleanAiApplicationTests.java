package fiap.com.br.Ocean.Clean.AI;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OceanCleanAiApplicationTests {

	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(OceanCleanAiApplication.class);
        // Define a porta usando a variável de ambiente 'PORT', padrão para 8080 se não estiver definida
        app.setDefaultProperties(Collections.singletonMap("server.port", getPort()));
        app.run(args);
    }

    private static String getPort() {
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "8080";  // Porta padrão
        }
        return port;
    }

}
