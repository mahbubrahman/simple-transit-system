package simple.transitsystem.mbta;

import simple.transitsystem.core.Route;
import simple.transitsystem.core.TransitSystem;
import simple.transitsystem.core.TransitSystemPrinter;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Simple TransitSystem in use by MBTA.
 */
public class Mbta {

    public static void main(String[] args) throws Exception {

        Set<Route> routes = new MbtaTransitSystemInitializer().getMbtaRouts("0", "1");
        TransitSystem transitSystem = new MbtaTransitSystemInitializer().initialize(routes);
        routes = transitSystem.getRoutes();

        TransitSystemPrinter transitSystemPrinter = new TransitSystemPrinter(System.out);

        Scanner scanner = new Scanner(System.in);
        int userChoice;

        do {
            userChoice = menu(scanner);
            switch (userChoice) {
                case 1:
                    transitSystemPrinter.printRouts(routes);
                    break;

                case 2:
                    transitSystemPrinter.printRoutsAndStopsInformation(transitSystem);
                    break;

                case 3:
                    System.out.println("\nGet direction between two stops using stop name >>");
                    System.out.println("For the full list of stop names, see: https://www.mbta.com/stops/subway#subway-tab\n");

                    System.out.println("Enter Origin: ");
                    scanner = new Scanner(System.in);
                    String originStopName = scanner.nextLine();

                    System.out.println("Enter Destination: ");
                    scanner = new Scanner(System.in);
                    String destinationStopName = scanner.nextLine();

                    List route = null;
                    try {
                        route =  transitSystem.getDirections(originStopName.trim().toLowerCase(),
                                destinationStopName.trim().toLowerCase());
                    } catch (Exception e) {
                        if(TransitSystem.ERROR_MASSAGE_INVALID_STOP_NAME.equals(e.getMessage())) {
                            System.out.println("One of the stop names was invalid.\n");
                        }
                        break;
                    }
                    System.out.println("\nDirection between " + originStopName + " and " + destinationStopName + " >>");
                    transitSystemPrinter.printDirections(route);
                    break;

                case 4:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.\n");
            }
        } while (userChoice != 4);

        scanner.close();
    }

    public static int menu(Scanner scanner) {
        int selection;
        System.out.println("Choose one from the following options:");
        System.out.println("[1] - Get subway routs                 [2] - Get rout with most stops, fewest stops and stops that connect two or more routes");
        System.out.println("[3] - Get direction between two stops  [4] - Quit \n");
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.println("Invalid option. Please try again.\n");
        }
        selection = scanner.nextInt();
        return selection;
    }
}
