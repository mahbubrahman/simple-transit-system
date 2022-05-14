package simple.transitsystem.mbta;

import jakarta.json.*;
import simple.transitsystem.core.Stop;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * Use following url to see the sample json value:
 * https://api-v3.mbta.com/routes/Red?include=route_patterns.representative_trip.stops
 */
public class MbtaRouteDataUnmarshaller {

    public static MbtaRouteData unmarshall(final InputStream is) {

        final MbtaRouteData.Builder builder = MbtaRouteData.Builder.create();

        JsonReader jsonReader = Json.createReader(is);
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        String routeId = jsonObject.getJsonObject("data").getString("id");
        builder.setId(routeId);

        String routeName = jsonObject.getJsonObject("data").getJsonObject("attributes").getString("long_name");
        builder.setName(routeName);

        String routeType = Integer.toString(jsonObject.getJsonObject("data").getJsonObject("attributes").getInt("type"));
        builder.setType(routeType);

        List<String> stopIds = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getJsonArray("included");

        if (jsonArray == null) {
            return builder.build();
        }

        for(JsonValue value : jsonArray) {
            JsonObject obj = value.asJsonObject();
            String type = obj.getString("type");

            if ("trip".equals(type)) {
                JsonArray stopsArray = obj.getJsonObject("relationships").getJsonObject("stops").getJsonArray("data");
                for (JsonValue each : stopsArray) {
                    stopIds.add(each.asJsonObject().getString("id"));
                }
                if (!stopIds.isEmpty()) {
                    builder.addRepresentativeTripStopIds(stopIds);
                    stopIds = new ArrayList<>();
                }
            }
            if ("stop".equals(type)) {
                String id = obj.getString("id");
                String name = obj.getJsonObject("attributes").getString("name");
                String parentStopId = null;
                JsonValue val = obj.getJsonObject("relationships").getJsonObject("parent_station").get("data");
                if (("null").equals(val.toString())) {
                    parentStopId = id;
                } else {
                    parentStopId = val.asJsonObject().getString("id");
                }
                Stop stop = new Stop(id, name, parentStopId);
                builder.addStop(stop);
            }
        }

        return builder.build();
    }

}
