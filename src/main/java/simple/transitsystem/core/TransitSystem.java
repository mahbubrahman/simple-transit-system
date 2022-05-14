package simple.transitsystem.core;

import java.util.*;

/**
 * A simple Transit System.
 */
public class TransitSystem {

    public static final String ERROR_MASSAGE_INVALID_STOP_NAME = "Invalid Stop Name";

    private Set<Route> routes = new HashSet<>();
    private TransitNetwork transitNetwork;
    private static Map<String, StationStop> stationByStationName = new HashMap<>();

    public Set<Route> getRoutes() {
        return routes;
    }

    public Route routeWithMaximumNumberOfStops() {
        return routes.stream().reduce((r1, r2) -> r1.getStationStops().size() > r2.getStationStops().size() ? r1 : r2).get();
    }

    public Route routeWithMinimumNumberOfStops() {
        return routes.stream().reduce((r1, r2) -> r1.getStationStops().size() < r2.getStationStops().size() ? r1 : r2).get();
    }

    public Map<StationStop, Set<String>> getMultipleRouteConnectingStationStops() {
        Map<StationStop, Set<String>> routeConnectingStops = new HashMap<>();
        for (Route route1 : routes) {
            for (Route route2 : routes) {
                if (route1 == route2) {
                    continue;
                }
                Set<StationStop> commonStops = route1.commonStationStops(route2);
                for (StationStop each : commonStops) {
                    if(routeConnectingStops.containsKey(each)) {
                        routeConnectingStops.get(each).addAll(new HashSet<String>(Arrays.asList(route1.getId(), route2.getId())));
                    } else {
                        routeConnectingStops.put(each, new HashSet<String>(Arrays.asList(route1.getId(), route2.getId())));
                    }
                }
            }
        }
        return routeConnectingStops;
    }

    public List<Connection> getDirections(String originStopName, String destinationStopName) {

        StationStop origin = stationByStationName.get(originStopName);
        StationStop destination = stationByStationName.get(destinationStopName);

        if (origin == null || destination == null) {
            throw new RuntimeException(ERROR_MASSAGE_INVALID_STOP_NAME);
        }
        return transitNetwork.getDirections(origin, destination);
    }

    private TransitSystem(Builder builder) {
        this.routes = builder.routes;
        transitNetwork = new TransitNetwork(builder.connections, builder.network);
        for (Route route : builder.routes) {
            for (StationStop stop : route.getStationStops()) {
                stationByStationName.put(stop.getName().toLowerCase(), stop);
            }
        }
    }

    /*
     * TransitSystem Builder
     */
    public static class Builder {
        private Set<Route> routes;
        private Set<Connection> connections;
        private Map<StationStop, LinkedList<StationStop>> network;

        public Builder() {
            routes = new HashSet<>();
            connections = new HashSet<>();
            network = new HashMap<>();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder addRoute(RouteData mbtaRoute) {
            this.routes.add(mbtaRoute.getRoute());

            for(List<String> stopIds : mbtaRoute.getRepresentativeTripsStopIds()) {
                for (int k = 0; k < stopIds.size() - 1; k++) {
                    StationStop stop1 = mbtaRoute.getStationByStopId().get(stopIds.get(k));
                    StationStop stop2 = mbtaRoute.getStationByStopId().get(stopIds.get(k + 1));
                    this.connections.add(new Connection(stop1, stop2, mbtaRoute.getRoute()));
                    addToNetwork(stop1, stop2);
                }
            }

            return this;
        }

        private void addToNetwork(StationStop stationStop1, StationStop stationStop2) {
            if (this.network.keySet().contains(stationStop1)) {
                LinkedList<StationStop> connectingStationStops = this.network.get(stationStop1);
                if (!connectingStationStops.contains(stationStop2)) {
                    connectingStationStops.add(stationStop2);
                }
            } else {
                LinkedList<StationStop> connectingStationStops = new LinkedList<>();
                connectingStationStops.add(stationStop2);
                this.network.put(stationStop1, connectingStationStops);
            }
        }

        public TransitSystem build() {
            return new TransitSystem(this);
        }

    }
}
