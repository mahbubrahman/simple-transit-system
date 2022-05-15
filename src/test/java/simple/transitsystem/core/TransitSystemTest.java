package simple.transitsystem.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;
import simple.transitsystem.mbta.MbtaRouteDataUnmarshaller;

import java.io.File;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransitSystemTest {

    private static final Logger logger = Logger.getLogger(TransitSystemTest.class.getName());

    private TransitSystem transitSystem;

    @BeforeAll
    void setup() throws FileNotFoundException {
        File initialFile = new File("src/test/resources/sample-full.json");
        InputStream targetStream = new FileInputStream(initialFile);
        RouteData routeData = MbtaRouteDataUnmarshaller.unmarshall(targetStream);
        transitSystem = TransitSystem.Builder.create().addRoute(routeData).build();
    }

    @BeforeEach
    void init() {
        logger.info("@BeforeEach - executes before each test method in this class");
    }

    @Test
    void testBasic() throws FileNotFoundException {
        Assertions.assertTrue(transitSystem != null);
        Assertions.assertEquals(1, transitSystem.getRoutes().size());
        Assertions.assertEquals("Red", transitSystem.getRoutes().stream().findFirst().get().getId());
        Assertions.assertEquals(22, transitSystem.routeWithMaximumNumberOfStops().getStationStops().size());
        Assertions.assertEquals(22, transitSystem.routeWithMinimumNumberOfStops().getStationStops().size());
        Assertions.assertEquals(0, transitSystem.getMultipleRouteConnectingStationStops().size());
        Assertions.assertEquals(2, transitSystem.getDirections("Kendall/MIT", "Harvard").size());
    }


    @AfterEach
    void tearDown() {
        logger.info("@AfterEach - executed after each test method.");
    }

    @AfterAll
    static void done() {
        logger.info("@AfterAll - executed after all test methods.");
    }
}
