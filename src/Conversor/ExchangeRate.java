package Conversor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ExchangeRate {
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6";
    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    private final String apiKey;

    public ExchangeRate() {
        // Lee de variable de entorno EXCHANGE_RATE_API_KEY o de propiedad -DAPI_KEY
        String env = System.getenv("EXCHANGE_RATE_API_KEY");
        String prop = System.getProperty("API_KEY");
        this.apiKey = (env != null && !env.isBlank()) ? env : prop;

        if (this.apiKey == null || this.apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Falta la API Key. Define EXCHANGE_RATE_API_KEY o usa -DAPI_KEY=tu_clave");
        }
    }

    /**
     * Devuelve el mapa "conversion_rates" (p.ej. {"USD":1, "CLP":930.1, ...})
     * usando la moneda base indicada.
     */
    public Map<String, Double> obtenerTasas(String base) {
        String url = String.format("%s/%s/latest/%s", BASE_URL, apiKey, base);
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();

        try {
            HttpResponse<String> res = HTTP.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() != 200) {
                throw new RuntimeException("HTTP " + res.statusCode() + " al consultar la API");
            }

            JsonObject json = GSON.fromJson(res.body(), JsonObject.class);

            // La API v6 entrega: { "result":"success", "conversion_rates":{...}, ... }
            String result = json.has("result") ? json.get("result").getAsString() : "error";
            if (!"success".equalsIgnoreCase(result)) {
                String msg = json.has("error-type") ? json.get("error-type").getAsString() : "desconocido";
                throw new RuntimeException("API no exitosa: " + msg);
            }

            JsonObject rates = json.getAsJsonObject("conversion_rates");
            // Gson puede mapear el objeto a Map<String, Double> directo
            @SuppressWarnings("unchecked")
            Map<String, Double> map = GSON.fromJson(rates, Map.class);
            return map;

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error de red al consultar la API", e);
        }
    }
}
