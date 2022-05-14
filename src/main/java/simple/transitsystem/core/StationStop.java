package simple.transitsystem.core;

import java.util.Objects;

/**
 * A transit system Station Stop.
 */
public class StationStop {

    private String id;
    private String name;

    public StationStop(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StationStop)) return false;
        StationStop stationStop = (StationStop) o;
        return Objects.equals(getId(), stationStop.getId()) &&
                Objects.equals(getName(), stationStop.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "StationStop{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
