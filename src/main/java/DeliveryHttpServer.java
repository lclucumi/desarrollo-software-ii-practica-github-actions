import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

// esta clase representa la parte que recibe solicitudes HTTP.
// Usa el servidor incluido en el JDK para que el ejemplo sea pequeño.
public class DeliveryHttpServer {

    // guardamos el servidor como atributo para poder iniciarlo,
    // consultar el puerto que usa y detenerlo más adelante.
    private final HttpServer server;

    // el constructor recibe el servicio que procesará la solicitud
    // y el puerto en el que queremos iniciar el servidor.
    public DeliveryHttpServer(DeliveryService service, int port)
            throws IOException {

        // el servidor escucha solo en esta misma computadora.
        // Si el puerto es cero, el sistema elige uno que esté disponible.
        server = HttpServer.create(
                new InetSocketAddress("127.0.0.1", port),
                0
        );

        // creamos una ruta llamada /deliveries.
        // Cuando llega una solicitud a esa ruta, llamamos a handleRequest.
        server.createContext(
                "/deliveries",
                exchange -> handleRequest(exchange, service)
        );
    }

    // este método pone el servidor a escuchar solicitudes.
    public void start() {
        server.start();
    }

    // como podemos solicitar un puerto automático,
    // este método nos permite saber cuál eligió el sistema.
    public int getPort() {
        return server.getAddress().getPort();
    }

    // al terminar las pruebas, detenemos el servidor.
    public void stop() {
        server.stop(0);
    }

    // este método atiende una solicitud que llegó a /deliveries.
    // HttpExchange contiene información de la solicitud y permite responder.
    private void handleRequest(
            HttpExchange exchange,
            DeliveryService service
    ) throws IOException {

        // para este ejemplo aceptamos únicamente solicitudes GET.
        // Si llega otro método, respondemos con el estado HTTP 405.
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "Método no permitido");
            return;
        }

        // leemos los parámetros que vienen después del signo ? en la URL.
        // Por ejemplo: transport=bicicleta&km=5&priority=true
        Map<String, String> parameters =
                readQueryParameters(
                        exchange.getRequestURI().getRawQuery()
                );

        // verificamos que la solicitud incluya los tres datos necesarios.
        // Si falta alguno, respondemos con el estado 400 y terminamos aquí.
        if (!parameters.containsKey("transport")
                || !parameters.containsKey("km")
                || !parameters.containsKey("priority")) {
            sendResponse(exchange, 400, "Faltan datos");
            return;
        }

        // el valor km llega como texto, así que intentamos convertirlo
        // a un número decimal para poder hacer el cálculo.
        double km;

        try {
            km = Double.parseDouble(parameters.get("km"));
        } catch (NumberFormatException error) {
            // si el texto no se puede convertir a número,
            // respondemos indicando que la distancia no es válida.
            sendResponse(exchange, 400, "La distancia no es válida");
            return;
        }

        // comprobamos que priority tenga exactamente uno
        // de los valores que esperamos: true o false.
        String priorityText = parameters.get("priority");

        if (!"true".equalsIgnoreCase(priorityText)
                && !"false".equalsIgnoreCase(priorityText)) {
            sendResponse(exchange, 400, "La prioridad no es válida");
            return;
        }

        // Una vez validado el texto, lo convertimos a boolean.
        boolean priority = Boolean.parseBoolean(priorityText);

        // aquí conectamos el servidor con la lógica de la aplicación.
        // El servidor entrega los datos al servicio y recibe el resultado.
        String result = service.createDelivery(
                parameters.get("transport"),
                km,
                priority
        );

        // si llegamos hasta aquí, enviamos el resultado con estado 200,
        // que indica que la solicitud se procesó correctamente.
        sendResponse(exchange, 200, result);
    }

    // este método convierte la parte de parámetros de la URL
    // en un mapa de nombres y valores.
    private Map<String, String> readQueryParameters(String query)
            throws IOException {

        // creamos un mapa vacío.
        // Cada nombre de parámetro será una clave y su valor será el contenido.
        Map<String, String> parameters = new HashMap<>();

        // si la URL no trae parámetros, devolvemos el mapa vacío.
        if (query == null || query.isEmpty()) {
            return parameters;
        }

        // separamos los parámetros usando &.
        // Por ejemplo, esto produce tres partes:
        // transport=bicicleta, km=5 y priority=true.
        String[] pairs = query.split("&");

        // recorremos cada parámetro por separado.
        for (String pair : pairs) {

            // dividimos cada parte por el primer signo igual.
            // El límite 2 evita dividir el valor si también contiene un igual.
            String[] parts = pair.split("=", 2);

            // solo guardamos la parte si tiene nombre y valor.
            if (parts.length == 2) {

                // decodificamos el nombre, por si contiene caracteres
                // que estén representados en formato URL.
                String name = URLDecoder.decode(
                        parts[0],
                        StandardCharsets.UTF_8.name()
                );

                // hacemos lo mismo con el valor.
                String value = URLDecoder.decode(
                        parts[1],
                        StandardCharsets.UTF_8.name()
                );

                // guardamos el par nombre-valor en el mapa.
                parameters.put(name, value);
            }
        }

        // devolvemos todos los parámetros que pudimos leer.
        return parameters;
    }

    // este método centraliza la creación y el envío de respuestas HTTP.
    // Recibe el estado, como 200 o 400, y el texto que verá quien hizo la solicitud.
    private void sendResponse(
            HttpExchange exchange,
            int status,
            String body
    ) throws IOException {

        // convertimos la respuesta a bytes usando UTF-8.
        // HTTP envía bytes, aunque nosotros construyamos un texto.
        byte[] response = body.getBytes(StandardCharsets.UTF_8);

        // indicamos que la respuesta es texto y usa codificación UTF-8.
        exchange.getResponseHeaders().set(
                "Content-Type",
                "text/plain; charset=UTF-8"
        );

        // enviamos el código de estado y la cantidad de bytes
        // que tendrá el cuerpo de la respuesta.
        exchange.sendResponseHeaders(status, response.length);

        // abrimos el flujo de salida, escribimos la respuesta
        // y lo cerramos automáticamente al salir del bloque.
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(response);
        }
    }
}
// fin de la clase DeliveryHttpServer