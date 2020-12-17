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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
        assertEquals(4, users.size());
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

        assertEquals(5, h2Repository.getUsers().size());
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
        assertEquals(2, orders.size());
    }

    @Test
    void createOrder() {
        Address ogAddress = new Address("Earth", "Belgium", "City", "Street", 1);
        Order newOrder = new Order(0, UUID.randomUUID(), 1, 1, 3, "status", 220, 50, 50, 50, 250, ogAddress);
        h2Repository.createOrder(newOrder, 1);

        assertEquals(3, h2Repository.getOrders().size());
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
    void getIdsForStatuses() {
        Map<Integer, String> statuses = new HashMap<>();
        statuses.put(1, "Travelling");
        statuses.put(2, "Returning");
        statuses.put(3, "Finished");

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
        assertThrows(MarsException.class, () -> {
            h2Repository.getRocketById(5000);
        });
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
