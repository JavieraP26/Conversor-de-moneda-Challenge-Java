package Conversor;

import java.util.Map;

public class Conversor {
    private final ExchangeRate client = new ExchangeRate();

    /**
     * Convierte 'monto' desde 'from' hacia 'to' usando tasas de ExchangeRate API.
     * Estrategia: pedimos tasas con base = from y tomamos la tasa directa a 'to'.
     */
    public double convertir(Moneda from, Moneda to, double monto) {
        if (from == to) return monto;

        Map<String, Double> rates = client.obtenerTasas(from.name()); // base = from
        Double rate = rates.get(to.name());
        if (rate == null) {
            throw new RuntimeException("No hay tasa para " + to);
        }
        return monto * rate;
    }
}
