package simple.transitsystem.core;

import java.util.*;

/**
 * This is a data container class that holds route data to initialize the Simple Transit System. To construct the
 * Transit System Network, the initializer needs to provide the route stops in appropriate sequence and directions.
 * The following resource relationships available in a transit system are used here to get the stops in proper order:
 *
 * Route has many route_patterns
 * RoutePattern has many trips
 * RoutePattern has one representative_trip
 * Trip has many stops (represented in proper sequence and direction)
 */
public class RouteData {

    private Route route;
    private Map<String, StationStop> stationByStopId = new HashMap<>();

    /*
     * Stops in different trips that follows the actual sequence and direction.
     * The TransitNetwork will be built using this stop sequence.
     */
    private List<List<String>> representativeTripsStopIds = new ArrayList<>();

    public Route getRoute() {
        return route;
    }

    public Map<String, StationStop> getStationByStopId() {
        return stationByStopId;
    }

    public List<List<String>> getRepresentativeTripsStopIds() {
        return representativeTripsStopIds;
    }

    private RouteData(Builder builder) {
        this.route = new Route(builder.routeId, builder.routeName, builder.routeType);
        for(Stop stop : builder.stops) {
            stationByStopId.put(stop.getId(), stop.getStationStop());
            this.route.getStationStops().add(stop.getStationStop());
        }
        this.representativeTripsStopIds = builder.representativeTripsStopIds;
    }

    /*
     * RouteData Builder.
     */
    public static class Builder {
        private String routeId;
        private String routeName;
        private String routeType;
        private Set<Stop> stops;
        private List<List<String>> representativeTripsStopIds;

        public Builder() {
            routeId = null;
            routeName = null;
            stops = new HashSet<>();
            representativeTripsStopIds = new ArrayList<>();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder setId(final String routeId) {
            this.routeId = routeId;
            return this;
        }

        public Builder setName(final String routeName) {
            this.routeName = routeName;
            return this;
        }

        public Builder setType(final String routeType) {
            this.routeType = routeType;
            return this;
        }

        public Builder addStop(Stop stop) {
            this.stops.add(stop);
            return this;
        }

        public Builder addRepresentativeTripStopIds(List<String> representativeTripStopsIds) {
            this.representativeTripsStopIds.add(representativeTripStopsIds);
            return this;
        }

        public RouteData build() {
            return new RouteData(this);
        }
    }

    @Override
    public String toString() {
        return "RouteData{" +
                "route=" + route +
                ", stopMap=" + stationByStopId +
                ", representativeTripsStopIds=" + representativeTripsStopIds +
                '}';
    }
}
