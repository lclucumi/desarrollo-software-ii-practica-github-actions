// el servicio coordina la solicitud y usa el calculador.
// En este ejemplo no guarda datos en una base de datos.
public class DeliveryService {

    private final DeliveryCostCalculator calculator;

    public DeliveryService(DeliveryCostCalculator calculator) {
        this.calculator = calculator;
    }

    // recibe los datos de una entrega y pide el cálculo.
    public String createDelivery(
            String transport,
            double km,
            boolean priority
    ) {
        double cost = calculator.calculate(km, priority);

        // devolvemos una respuesta sencilla para la demostración.
        return "transporte=" + transport + "; costo=" + cost;
    }
}