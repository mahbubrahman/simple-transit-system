package simple.transitsystem.mbta;

import jakarta.json.*;
import simple.transitsystem.core.Route;
import simple.transitsystem.core.RouteData;
import simple.transitsystem.core.TransitSystem;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * Initializes the Simple Transit System for MBTA.
 */
public class MbtaTransitSystemInitializer {

    private static final String API_KEY = "";

    private static final String API_HOST_URL = "https://api-v3.mbta.com";

    /*
     * Use Java HttpClient to access api endpoints.
     */
    private InputStream jsonResponseStream(String resource) throws Exception {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(API_HOST_URL + resource))
                .GET();

        if (API_KEY != null && !API_KEY.isBlank()) {
            requestBuilder.headers("x-api-key", API_KEY);
        }

        HttpResponse<InputStream> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofInputStream());
        return response.body();
    }

    /*
     * Get routes by route types. If routeTypes are empty, then it gets all routes.
     *
     * See https://api-v3.mbta.com/docs/swagger/index.html#/Route/ApiWeb_RouteController_index
     * for the sample json value.
     */
    public Set<Route> getMbtaRouts(String... routeTypes) throws Exception {

        Set<Route> routes = new HashSet<>();

        String routeResource = "/routes?filter[type]=" + String.join(",", routeTypes);
        JsonReader jsonReader = Json.createReader(jsonResponseStream(routeResource));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        for (JsonValue each : jsonObject.getJsonArray("data")) {
            String id = each.asJsonObject().getString("id");
            String name = each.asJsonObject().getJsonObject("attributes").getString("long_name");
            String type = String.valueOf(each.asJsonObject().getJsonObject("attributes").getInt("type"));
            routes.add(new Route(id, name, type));
        }
        return routes;
    }

    /*
     * Initializes the TransitSystem for the given routes. It uses nested includes to avoid N+1 requests to read
     * all stops along a representative trip. See RouteData for more details.
     *
     * For more details about nested includes, see: https://www.mbta.com/developers/v3-api/best-practices
     */
    public TransitSystem initialize(Set<Route> routes) throws Exception {

        TransitSystem.Builder builder = TransitSystem.Builder.create();
        RouteData mbtaRouteData = null;
        for (Route each : routes) {
            String resource = "/routes/" + each.getId() + "?include=route_patterns.representative_trip.stops";
            mbtaRouteData = MbtaRouteDataUnmarshaller.unmarshall(jsonResponseStream(resource));
            builder.addRoute(mbtaRouteData);
        }
        return builder.build();
    }
}
