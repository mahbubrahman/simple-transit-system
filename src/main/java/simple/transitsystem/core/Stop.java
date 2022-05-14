package simple.transitsystem.core;

import java.util.Objects;

/**
 * A transit system Stop. Stop can optionally be associated with a StationStop.
 * A Stop without an associated StationStop becomes its own StationStop by sharing the same id.
 */
public class Stop {

    private String id;
    private StationStop stationStop;

    public Stop(String id, String name, String stationId) {
        this.id = id;
        this.stationStop = new StationStop(stationId, name);
    }

    public String getId() {
        return id;
    }

    public StationStop getStationStop() {
        return stationStop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stop)) return false;
        Stop stop = (Stop) o;
        return getId().equals(stop.getId()) && getStationStop().equals(stop.getStationStop());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStationStop());
    }

    @Override
    public String toString() {
        return "Stop{" +
                "id='" + id + '\'' +
                ", stationStop=" + stationStop +
                '}';
    }
}
