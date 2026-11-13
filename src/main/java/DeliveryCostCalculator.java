// empezamos con la regla pequeña que queremos comprobar
// de forma aislada.
public class DeliveryCostCalculator {

    // recibe la distancia y si la entrega es prioritaria.
    public double calculate(double km, boolean priority) {
        // calculamos primero el costo base.
        double base = km * 2000;

        // solo agregamos el 25 por ciento si es prioritaria.
        if (priority) {
            return base * 1.25;
        }

        // si no es prioritaria, devolvemos el costo base.
        return base;
    }
}