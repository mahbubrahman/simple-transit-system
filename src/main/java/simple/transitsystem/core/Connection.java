package simple.transitsystem.core;

import java.util.Objects;

/**
 * Connections between two stops in a route based on the trips different transportation
 * vehicles make in Transit Network.
 */
public class Connection {

    private StationStop stationStop1;
    private StationStop stationStop2;
    private Route route;

    public Connection(StationStop stationStop1, StationStop stationStop2, Route route) {
        this.stationStop1 = stationStop1;
        this.stationStop2 = stationStop2;
        this.route = route;
    }

    public StationStop getStationStop1() {
        return stationStop1;
    }

    public StationStop getStationStop2() {
        return stationStop2;
    }
    public Route getRoute() {
        return route;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connection)) return false;
        Connection that = (Connection) o;
        return Objects.equals(getStationStop1(), that.getStationStop1()) &&
                Objects.equals(getStationStop2(), that.getStationStop2()) &&
                Objects.equals(getRoute().getId(), that.getRoute().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStationStop1(), getStationStop2(), getRoute().getId());
    }

    @Override
    public String toString() {
        return "Connection{" +
                "stationStop1=" + stationStop1 +
                ", stationStop2=" + stationStop2 +
                ", route=" + route +
                '}';
    }
}
