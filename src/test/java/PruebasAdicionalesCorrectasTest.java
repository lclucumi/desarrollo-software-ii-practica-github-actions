import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

// Este caso corresponde a la regla real del sistema.
class PruebasAdicionalesCorrectasTest {
    @Test
    void cincoKmSinPrioridadCuestan10000() {
        DeliveryCostCalculator calculador = new DeliveryCostCalculator();
        double resultado = calculador.calculate(5.0, false);
        assertEquals(10000.0, resultado, 0.001);
    }
}
