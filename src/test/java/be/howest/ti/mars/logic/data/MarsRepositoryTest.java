package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.util.MarsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MarsRepositoryTest {
    private final Logger LOGGER = Logger.getLogger(MarsRepository.class.getName());
    private final MarsRepository marsRepository = new MarsRepository();
    private final OrderRepository orderRepository = new H2OrderRepository();
    private static final String URL = "jdbc:h2:~/test";

    @BeforeAll
    void setupTestSuite() throws SQLException {
        MarsRepository.configure(URL, "sa", "", 9000);
    }

    @BeforeEach
    void setupTest() throws IOException {
        createDatabase();
    }

    @Test
    void getUsers() {
        List<User> users = marsRepository.getUsers();
        assertEquals(2, users.size());
    }

    @Test
    void getRole() {
        Role role = new Role("Admin", 1);
        assertEquals(role, marsRepository.getRoleViaEmail("maarten.demeyere@hotmail.com"));
    }

    @Test
    void createUser() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 1);
        User newUser = new User("Jos", "Vermeulen", "0412345678", "jos@lol.be", "pass", address);
        marsRepository.createUser(newUser);
        assertEquals(3, marsRepository.getUsers().size());

        Role userRole = new Role("User", 2);
        assertEquals(userRole, marsRepository.getRoleViaEmail("jos@lol.be"));
    }

    @Test
    void getUserViaMail() {
        assertNotNull(marsRepository.getUserViaEmail("maarten.demeyere@hotmail.com"));
    }

    @Test
    void getUserViaLogin() {
        assertNotNull(marsRepository.getUserViaLogin("maarten.demeyere@hotmail.com", "pass"));
    }

    @Test
    void getOrders() {
        List<Order> orders = orderRepository.getOrders();
        assertEquals(2, orders.size());
    }

    @Test
    void createOrder() {
        Order newOrder = new Order(0, 1, 1, 3, 220, 50, 50, 50, 250);
        orderRepository.createOrder(newOrder);

        assertEquals(3, orderRepository.getOrders().size());
    }

    @Test
    void getRockets() {
        List<Rocket> rockets = marsRepository.getRockets();
        assertEquals(2, rockets.size());
    }

    private void createDatabase() throws IOException {
        executeScript("src/test/resources/testdb-create.sql");
        executeScript("src/test/resources/testdb-populate.sql");
    }

    private void executeScript(String fileName) throws IOException {
        String createDbSql = readFile(fileName);
        try (Connection con = MarsRepository.getConnection();
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
