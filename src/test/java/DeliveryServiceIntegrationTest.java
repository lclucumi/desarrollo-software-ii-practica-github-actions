import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

// esta prueba usa juntos el servicio y el calculador real.
class DeliveryServiceIntegrationTest {

    @Test
    void serviceUsesCalculatorToCreateDelivery() {
        // conectamos el servicio con el calculador.
        DeliveryCostCalculator calculator =
                new DeliveryCostCalculator();
        DeliveryService service = new DeliveryService(calculator);

        // el servicio recibe la solicitud y produce la respuesta.
        String result = service.createDelivery(
                "bicicleta",
                5.0,
                true
        );

        // comprobamos el resultado de la colaboración
        // entre el servicio y el calculador.
        assertEquals(
                "transporte=bicicleta; costo=12500.0",
                result
        );
    }
}