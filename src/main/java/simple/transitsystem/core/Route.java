package simple.transitsystem.core;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Transit system Route with StationStop Stops.
 */
public class Route {

    private String id;
    private String name;
    private String type;

    /* All stationStops in this route, without any particular sequence or direction */
    private Set<StationStop> stationStops = new HashSet<>();

    public Route(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    /**
     * Stops in this route connecting the other route.
     *
     * @param otherRoute The other route
     * @return The Set of common or connected stops
     */
    public Set<StationStop> commonStationStops(Route otherRoute) {
        return  this.stationStops.stream()
                .filter(otherRoute.getStationStops()::contains)
                .collect(Collectors.toSet());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Set<StationStop> getStationStops() {
        return stationStops;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return Objects.equals(getId(), route.getId()) &&
                Objects.equals(getName(), route.getName()) &&
                Objects.equals(getType(), route.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getType());
    }

    @Override
    public String toString() {
        return "Route{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
