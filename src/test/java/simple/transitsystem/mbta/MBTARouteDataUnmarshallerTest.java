package simple.transitsystem.mbta;

import org.junit.jupiter.api.*;
import simple.transitsystem.core.TransitSystemTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MBTARouteDataUnmarshallerTest {

    private static final Logger logger = Logger.getLogger(TransitSystemTest.class.getName());

    MbtaRouteData routeData;

    @BeforeAll
    void setup() throws FileNotFoundException {
        File initialFile = new File("src/test/resources/sample.json");
        InputStream targetStream = new FileInputStream(initialFile);
        routeData = MbtaRouteDataUnmarshaller.unmarshall(targetStream);
    }

    @BeforeEach
    void init() {
        logger.info("@BeforeEach - executes before each test method in this class");
    }

    @Test
    void testBasic() {
        Assertions.assertTrue(routeData != null);
        Assertions.assertEquals("Mattapan", routeData.getRoute().getId());
        Assertions.assertEquals(8, routeData.getRoute().getStationStops().size());
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
