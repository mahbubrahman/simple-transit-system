package simple.transitsystem.core;

import java.util.*;

/**
 * The Transit Network of the Simple Transit System.
 */
public class TransitNetwork {

    /*
     * All connections available in the network.
     */
    private Set<Connection> connections = new HashSet<>();

    /*
     * Represents the neighbors of a stop.
     */
    private Map<StationStop, LinkedList<StationStop>> network = new HashMap<>();

    public TransitNetwork(Set<Connection> connections, Map<StationStop, LinkedList<StationStop>> network) {
        this.connections = connections;
        this.network = network;
    }

    public Set<Connection> getConnections() {
        return connections;
    }

    public Map<StationStop, LinkedList<StationStop>> getNetwork() {
        return network;
    }

    public Set<StationStop> allStops() {
        Set<StationStop> allStops = new HashSet<>();
        for (Connection each : connections) {
            allStops.add(each.getStationStop1());
            allStops.add(each.getStationStop2());
        }
        return allStops;
    }

    public List<Connection> getDirections(StationStop origin, StationStop destination) {

        List<Connection> route = new LinkedList();
        Set<StationStop> reachableStations = new HashSet<>();
        Map<StationStop, StationStop> previousStations = new HashMap();
        List<StationStop> neighbors = network.get(origin);

        for (StationStop neighbor : neighbors) {
            if (neighbor.equals(destination)) {
                route.add(getConnection(origin, destination));
                return route;
            } else {
                reachableStations.add(neighbor);
                previousStations.put(neighbor, origin);
            }
        }

        List<StationStop> nextStops = new LinkedList();
        nextStops.addAll(neighbors);
        StationStop currentStop = origin;


        boolean keepLooping = true;

        for (int i = 1; i < allStops().size(); i++) {
            List<StationStop> tmpNextStops = new LinkedList();
            for (StationStop nextStop : nextStops) {
                reachableStations.add(nextStop);
                currentStop = nextStop;
                List<StationStop> currentNeighbors = network.get(currentStop);
                for (StationStop neighbor : currentNeighbors) {
                    if (neighbor.equals(destination)) {
                        reachableStations.add(neighbor);
                        previousStations.put(neighbor, currentStop);
                        keepLooping = false;
                        break;
                    } else if (!reachableStations.contains(neighbor)) {
                        reachableStations.add(neighbor);
                        tmpNextStops.add(neighbor);
                        previousStations.put(neighbor, currentStop);
                    }
                }
                if (!keepLooping) {
                    break;
                }
            }
            if (!keepLooping) {
                break;
            }
            nextStops = tmpNextStops;
        }

        StationStop keyStop = destination;
        StationStop stop;
        keepLooping = true;
        while (keepLooping) {
            stop = previousStations.get(keyStop);
            route.add(0, getConnection(stop, keyStop));
            if (origin.equals(stop)) {
                keepLooping = false;
            }
            keyStop = stop;
        }

        return route;
    }

    private Connection getConnection(StationStop stationStop1, StationStop stationStop2) {
        for (Connection each : connections) {
            StationStop one = each.getStationStop1();
            StationStop two = each.getStationStop2();
            if ((stationStop1.equals(one)) && stationStop2.equals(two)) {
                return each;
            }
        }
        return null;
    }

}
