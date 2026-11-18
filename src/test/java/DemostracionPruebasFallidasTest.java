import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

// ATENCION: expectativas incorrectas a proposito para mostrar dos fallos.
// La regla real es 2000 por kilometro y recargo prioritario del 25 %.
class DemostracionPruebasFallidasTest {
    @Test
    void falloIntencionalCincoKmConPrioridad() {
        DeliveryCostCalculator calculador = new DeliveryCostCalculator();
        double resultado = calculador.calculate(5.0, true);
        // Fallo intencional: 15000 es incorrecto; el esperado real es 12500.
        assertEquals(15000.0, resultado, 0.001);
    }

    @Test
    void falloIntencionalCincoKmSinPrioridad() {
        DeliveryCostCalculator calculador = new DeliveryCostCalculator();
        double resultado = calculador.calculate(5.0, false);
        // Fallo intencional: 12000 es incorrecto; el esperado real es 10000.
        assertEquals(12000.0, resultado, 0.001);
    }
}
