package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.util.MarsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class H2RepositoryTest {
    private final Logger LOGGER = Logger.getLogger(H2Repository.class.getName());
    private final MarsRepository h2Repository = H2Repository.getInstance();
    private static final String URL = "jdbc:h2:~/test";

    @BeforeAll
    void setupTestSuite() throws SQLException {
        H2Repository.configure(URL, "sa", "", 9000);
    }

    @BeforeEach
    void setupTest() throws IOException {
        createDatabase();
    }

    @Test
    void getUsers() {
        List<User> users = h2Repository.getUsers();
        assertEquals(5, users.size());
    }

    @Test
    void getRole() {
        Role role = new Role("Admin", 1);
        assertEquals(role, h2Repository.getRoleViaEmail("maarten.demeyere@hotmail.com"));
    }

    @Test
    void createUser() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 1);
        User newUser = new User("Jos", "Vermeulen", "0412345678", "jos@lol.be", "pass", address);

        h2Repository.createUser(newUser);

        assertEquals(6, h2Repository.getUsers().size());
    }

    @Test
    void userGetsRoleWhenCreated() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 1);
        User newUser = new User("Jos", "Vermeulen", "0412345678", "jos@lol.be", "pass", address);

        h2Repository.createUser(newUser);
        Role userRole = new Role("User", 2);

        assertEquals(userRole, h2Repository.getRoleViaEmail("jos@lol.be"));
    }

    @Test
    void userIsRemovedOnFailure() {
        try (Connection con = H2Repository.getConnection();
             PreparedStatement stmt = con.prepareStatement("delete from orders; delete from userroles;" +
                     "delete from users; delete from roles where rank = 2")) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Could not delete role!");
        }

        assertEquals(0, h2Repository.getUsers().size());

        try {
            Address address = new Address("Earth", "Belgium", "City", "Street", 1);
            User newUser = new User("Jos", "Vermeulen", "0412345678", "jos@lol.be", "pass", address);

            h2Repository.createUser(newUser);
        } catch (MarsException ex) {
            assertEquals(0, h2Repository.getUsers().size());
        }
    }

    @Test
    void setUser() {
        Address ogAddress = new Address("Earth", "Belgium", "City", "Street", 1);
        User ogUser = new User("Jos", "Vermeulen", "0412345678", "jos@lol.be", "pass", ogAddress);

        Address moddedAddress = new Address("Mars", "France", "Stad", "Straat", 500);
        User moddedUser = new User("Janneke", "Jan", "123456", "jos@lol.be", "lol", moddedAddress);

        h2Repository.createUser(ogUser);
        h2Repository.setUser(ogUser, moddedUser);

        assertEquals(moddedUser, h2Repository.getUserViaEmail("jos@lol.be"));
    }

    @Test
    void getUserViaMail() {
        assertNotNull(h2Repository.getUserViaEmail("maarten.demeyere@hotmail.com"));
    }

    @Test
    void getIdViaEmail() {
        assertEquals(4, h2Repository.getIdViaEmail("mira.vogelsang@telenet.com"));
    }

    @Test
    void getOrders() {
        List<Order> orders = h2Repository.getOrders();
        assertEquals(4, orders.size());
    }

    @Test
    void createOrder() {
        Address ogAddress = new Address("Earth", "Belgium", "City", "Street", 1);
        Order newOrder = new Order(0, UUID.randomUUID(), 1, 1, 3, "status", 220, 50, 50, 50, 250, ogAddress);
        h2Repository.createOrder(newOrder, 1);

        assertEquals(5, h2Repository.getOrders().size());
    }

    @Test
    void getOrderById() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 1);
        Order newOrder = new Order(15, UUID.randomUUID(), 1, 1, 3, "status", 220, 50, 50, 50, 250, address);
        h2Repository.createOrder(newOrder, 1);

        assertEquals(newOrder, h2Repository.getOrderById(newOrder.getOrderId()));
    }

    @Test
    void getOrderByUuid() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 1);
        Order newOrder = new Order(20, UUID.randomUUID(), 1, 1, 3, "status", 220, 50, 50, 50, 250, address);
        h2Repository.createOrder(newOrder, 1);

        assertEquals(newOrder, h2Repository.getOrderByUuid(newOrder.getUuid()));
    }

    @Test
    void getOrdersForUser() {
        assertEquals(2, h2Repository.getOrdersForUser("maarten.demeyere@hotmail.com").size());
    }

    @Test
    void updateOrderStatus() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 1);
        Order newOrder = new Order(20, UUID.randomUUID(), 1, 1, 1, "status", 220, 50, 50, 50, 250, address);
        h2Repository.createOrder(newOrder, 1);

        h2Repository.updateOrderStatus(newOrder.getOrderId(), 2);

        assertEquals(2, h2Repository.getOrderById(newOrder.getOrderId()).getStatusId());
    }

    @Test
    void getIdsForStatuses() {
        Map<Integer, String> statuses = new HashMap<>();
        statuses.put(1, "Processing");
        statuses.put(2, "Travelling");
        statuses.put(3, "Arrived");

        assertEquals(statuses, h2Repository.getIdsForStatuses());
    }

    @Test
    void getRockets() {
        List<Rocket> rockets = h2Repository.getRockets();
        assertEquals(66, rockets.size());
    }

    @Test
    void getRocketById() {
        Rocket rocket = new Rocket(1,"Falcon Heavy", "Mars","2055-12-18 13:30:00", "2055-01-18 08:20:30", 100.0f, 10000.0f, 2700.0f, 10000.0f, 2700.0f);

        assertEquals(rocket, h2Repository.getRocketById(1));
        assertThrows(IllegalStateException.class, () -> h2Repository.getRocketById(5000));
    }

    @Test
    void testUpdateRocketAvailableMassAndVolume() {
        Rocket rocket = new Rocket(1,"Falcon Heavy", "Mars","2055-12-18 13:30:00", "2055-01-18 08:20:30", 100.0f, 10000.0f, 2700.0f, 9600.0f, 2500.0f);
        h2Repository.updateRocketAvailableMassAndVolume(1, 9600.0f, 2500.f);

        assertEquals(rocket, h2Repository.getRocketById(1));
    }

    @Test
    void testGetFilteredRocketsFastUrgency() {
        LocalDateTime[] datesFast = new LocalDateTime[2];
        datesFast[0] = LocalDateTime.of(2055, Month.DECEMBER, 17, 15, 30, 0);
        datesFast[1] = LocalDateTime.of(2055, Month.DECEMBER, 19, 15, 30, 0);
        List<Rocket> rocketsFast = new ArrayList<>() {{
            add(new Rocket(1, "Falcon Heavy", "Mars", "2055-12-18 13:30:00", "2055-01-18 08:20:30", 100.0f, 10000.0f, 2700.0f, 10000.0f, 2700.0f));
            add(new Rocket(2, "Shear Razor", "Earth", "2055-12-19 12:15:20", "2055-01-19 22:30:00", 120.0f, 15000.0f, 1100.0f, 15000.0f, 1100.0f));
        }};

        assertEquals(rocketsFast, h2Repository.getFilteredRockets(200, 200, datesFast));
        assertEquals(1, h2Repository.getFilteredRockets(1500, 1500, datesFast).size());
    }

    @Test
    void testGetFilteredRocketsNormalUrgency() {
        LocalDateTime[] datesNormal = new LocalDateTime[2];
        datesNormal[0] = LocalDateTime.of(2055, Month.DECEMBER, 17, 15, 30, 0);
        datesNormal[1] = datesNormal[0].plusDays(5);
        List<Rocket> rocketsNormal = new ArrayList<>() {{
            add(new Rocket(1, "Falcon Heavy", "Mars", "2055-12-18 13:30:00", "2055-01-18 08:20:30", 100.0f, 10000.0f, 2700.0f, 10000.0f, 2700.0f));
            add(new Rocket(2, "Shear Razor", "Earth", "2055-12-19 12:15:20", "2055-01-19 22:30:00", 120.0f, 15000.0f, 1100.0f, 15000.0f, 1100.0f));
            add(new Rocket(3, "Starship", "Mars", "2055-12-20 12:15:20", "2055-01-20 22:30:00", 110.0f, 1600.0f, 1150.0f, 1600.0f, 1150.0f));
            add(new Rocket(4, "Cataphract", "Earth", "2055-12-21 12:15:20", "2055-01-21 22:30:00", 115.0f, 16000.0f, 5000.0f, 16000.0f, 5000.0f));
            add(new Rocket(5, "Maiden Voyage", "Mars", "2055-12-22 12:15:20", "2055-01-22 22:30:00", 110.0f, 1300.0f, 560.0f, 1300.0f, 560.0f));
        }};

        assertEquals(rocketsNormal, h2Repository.getFilteredRockets(200, 200, datesNormal));
        assertEquals(2, h2Repository.getFilteredRockets(1000, 2000, datesNormal).size());
    }

    @Test
    void testGetFilteredRocketsSlowUrgency() {
        LocalDateTime[] datesSlow = new LocalDateTime[2];
        datesSlow[0] = LocalDateTime.of(2055, Month.DECEMBER, 22, 15, 30, 0);
        datesSlow[1] = datesSlow[0].plusMonths(3);

        assertEquals(61, h2Repository.getFilteredRockets(50, 50, datesSlow).size());
    }

    @Test
    void testGetFilteredRocketsWrongDates() {
        LocalDateTime[] datesWrong = new LocalDateTime[2];
        datesWrong[0] = LocalDateTime.of(2055, Month.DECEMBER, 22, 15, 30, 0);
        datesWrong[1] = datesWrong[0].minusDays(2);

        assertEquals(0, h2Repository.getFilteredRockets(50, 50, datesWrong).size());
    }

    private void createDatabase() throws IOException {
        executeScript("src/test/resources/testdb-create.sql");
        executeScript("src/test/resources/testdb-populate.sql");
    }

    private void executeScript(String fileName) throws IOException {
        String createDbSql = readFile(fileName);
        try (Connection con = H2Repository.getConnection();
             PreparedStatement stmt = con.prepareStatement(createDbSql)) {

            stmt.executeUpdate();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Database could not be made!");
        }
    }

    private String readFile(String fileName) throws IOException {
        Path file = Path.of(fileName);
        return Files.readString(file);
    }
}
