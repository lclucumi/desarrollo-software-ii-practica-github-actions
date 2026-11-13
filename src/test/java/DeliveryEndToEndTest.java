import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// esta prueba recorre la solicitud HTTP,
// el servidor, el servicio y el calculador.
class DeliveryEndToEndTest {

    private DeliveryHttpServer server;

    @BeforeEach
    void startServer() throws IOException {
        // preparamos las piezas reales y levantamos el servidor local.
        DeliveryCostCalculator calculator =
                new DeliveryCostCalculator();
        DeliveryService service = new DeliveryService(calculator);

        server = new DeliveryHttpServer(service, 0);
        server.start();
    }

    @AfterEach
    void stopServer() {
        // al terminar la prueba cerramos el servidor.
        server.stop();
    }

    @Test
    void requestReturnsDeliveryCost() throws Exception {
        // enviamos una solicitud HTTP real al servidor local.
        URL url = new URL(
                "http://127.0.0.1:"
                        + server.getPort()
                        + "/deliveries?transport=bicicleta&km=5&priority=true"
        );

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // observamos el código y el cuerpo de la respuesta.
        int status = connection.getResponseCode();
        String body;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream(),
                        StandardCharsets.UTF_8
                )
        )) {
            body = reader.readLine();
        }

        connection.disconnect();

        System.out.println("Estado HTTP: " + status);
        System.out.println("Respuesta: " + body);
        // comprobamos el resultado visible desde fuera del sistema.
        assertEquals(200, status);
        assertEquals(
                "transporte=bicicleta; costo=12500.0",
                body
        );
    }
}