import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

// esta prueba ejecuta únicamente el calculador.
class DeliveryCostCalculatorTest {

    @Test
    void fiveKmPriorityCosts12500() {
        // preparamos el objeto que vamos a probar.
        DeliveryCostCalculator calculator =
                new DeliveryCostCalculator();

        // ejecutamos el cálculo para cinco kilómetros prioritarios.
        double result = calculator.calculate(5.0, true);
        System.out.println("Resultado obtenido: " + result);

        // comparamos lo obtenido con los 12.500 esperados.
        assertEquals(12500.0, result, 0.001);
    }
}