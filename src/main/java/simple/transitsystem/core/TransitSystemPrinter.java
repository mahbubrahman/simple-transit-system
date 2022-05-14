package simple.transitsystem.core;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Transit System Printer.
 */
public class TransitSystemPrinter {

    private PrintStream out;

    public TransitSystemPrinter(OutputStream out) {
        this.out = new PrintStream(out);
    }    

    public void printRouts(Set<Route> routes) {

        out.println("MBTA Routes >>");
        out.println(routes.stream().map(r -> r.getName()).collect(Collectors.joining(", ")));
        out.println("\n");
    }

    public void printRoutsAndStopsInformation(TransitSystem transitSystem ) {

        Set<Route> routes = transitSystem.getRoutes();

        out.println("Route with the most stops >>");
        out.println(transitSystem.routeWithMaximumNumberOfStops().getName() +
                " (Total number of stops: " + transitSystem.routeWithMaximumNumberOfStops().getStationStops().size() + ")\n");

        out.println("Route with the fewest stops >>");
        out.println(transitSystem.routeWithMinimumNumberOfStops().getName() +
                " (Total number of stops:" + transitSystem.routeWithMinimumNumberOfStops().getStationStops().size() + ")\n");

        out.println("List of the stops that connect two or more routes >>");
        transitSystem.getMultipleRouteConnectingStationStops().forEach((k, v) -> out.println((k.getName() + " - connects routes: " + v)));
        out.println("\n");
    }

    public void printDirections(List<Connection> route) {

        Connection connection = route.stream().findFirst().get();
        String currentLine = connection.getRoute().getName();
        String previousLine = currentLine;

        out.println("Start at " + connection.getStationStop1().getName());
        out.println("Get on the " + currentLine + " at " + connection.getStationStop1().getName() +
                " heading towards " + connection.getStationStop2().getName() + ".");

        for (Connection each : route) {
            currentLine = each.getRoute().getName();
            if (currentLine.equals(previousLine)) {
                out.println("  " + each.getStationStop1().getName() + " to " + each.getStationStop2().getName() + " > " + currentLine);
            } else {
                out.print("  When at " + each.getStationStop1().getName() + ", get off the " + previousLine + ". ");
                out.println("Transfer to the " + currentLine + ", heading towards " + each.getStationStop2().getName() + ".");
                previousLine = currentLine;
            }
            connection = each;
        }
        out.println("Get off at the destination " + connection.getStationStop2().getName() + "\n");
    }
}
