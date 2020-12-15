package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.util.MarsException;
import org.h2.tools.Server;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.channels.NotYetBoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
MBL: this is only a starter class to use a H2 database.
To make this class useful, please complete it with the topics seen in the module OOA & SD
- Make sure the conf/config.json properties are correct.
- The h2 web console is available at http://localhost:9000
- The h2 database file is located at ~/mars-db
- Hint:
  - Mocking this repository is not needed. Create database creating and population script in plain SQL.
    Use the @Before or @Before each (depending on the type of test) to quickly setup a fully populated db.
 */
public class H2Repository implements MarsRepository {
    private static final H2Repository INSTANCE = new H2Repository();
    private final Logger logger = Logger.getLogger(H2Repository.class.getName());

    private Server dbWebConsole;
    private String username;
    private String password;
    private String url;

    private H2Repository() {
        // Should not be called
    }

    public static H2Repository getInstance() {
        return INSTANCE;
    }

    public void cleanUp() {
        dbWebConsole.stop();
    }

    public static void configure(String url, String username, String password, int console)
            throws SQLException {
        INSTANCE.username = username;
        INSTANCE.password = password;
        INSTANCE.url = url;
        INSTANCE.dbWebConsole = Server.createWebServer(
                "-ifNotExists",
                "-webPort", String.valueOf(console)).start();
    }

    private static final String SQL_INSERT_USER = "insert into Users(first_name, last_name, email, phone_number, password, planet, country_or_colony, city_or_district, street, number) " +
            "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_BIND_ROLE_TO_USER = "insert into userroles(user_id, role_id) values(?, ?)";
    private static final String SQL_REMOVE_USER = "delete from users where email = ?";
    private static final String SQL_SELECT_USER_VIA_ID = "select * from users where id = ?";
    private static final String SQL_UPDATE_USER = "update users set first_name = ?, last_name = ?, email = ?, phone_number = ?, " +
            "password = ?, planet = ?, country_or_colony = ?, city_or_district = ?, street = ?, number = ? where email = ?";
    private static final String SQL_SELECT_ALL_USERS = "select * from Users";
    private static final String SQL_SELECT_USER_VIA_EMAIL = "select * from Users where email = ?";
    private static final String SQL_GET_ROLE_VIA_EMAIL = "select * from roles join userroles on roles.id = userroles.role_id " +
            "join users on userroles.user_id = users.id where email = ?";

    private static final String SQL_SELECT_ALL_ROCKETS = "select * from rockets";
    private static final String SQL_SELECT_ROCKET_VIA_ID = "select * from rockets where id = ?";
    private static final String SQL_UPDATE_ROCKET = "update rockets set available_mass = ?, available_volume = ? where id = ?";

    private static final String SQL_SELECT_ALL_ORDERS = "select * from orders";
    private static final String SQL_INSERT_ORDER = "insert into Orders(user_id, rocket_id, status_id, mass, width, height, depth, cost, planet, country_or_colony, city_or_district, street, number) " +
            "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ORDER_VIA_ID = "select * from orders where id = ?";
    private static final String SQL_SELECT_ORDERS_FOR_USER = "select * from orders where user_id = (select id from users where email = ?)";
    private static final String SQL_SELECT_STATUS_ID_AND_NAME = "select * from statuses";

    // User methods:
    @Override
    public void createUser(User user) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            createUserStatement(stmt, user);

            stmt.executeUpdate();

            try (ResultSet rsKey = stmt.getGeneratedKeys()) {
                rsKey.next();

                bindUserRole(rsKey.getInt(1));
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Could not create new user!");
        }
    }

    private void bindUserRole(int userId) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_BIND_ROLE_TO_USER)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, 2);

            stmt.executeUpdate();

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            removeUser(getUserViaId(userId));
            throw new MarsException("Could not bind user to role!");
        }
    }

    private void removeUser(User user) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_REMOVE_USER)) {

            stmt.setString(1, user.getEmail());

            stmt.executeUpdate();

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Could not remove the user!");
        }
    }

    private User getUserViaId(int id) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SELECT_USER_VIA_ID)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return getUserFromResultSet(rs);
            }

        } catch (SQLException ex) {
            throw handleCouldNotFindUser(ex);
        }
    }

    @Override
    public void setUser(User ogUser, User moddedUser) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_UPDATE_USER)) {

            createUserStatement(stmt, moddedUser);

            stmt.setString(11, ogUser.getEmail());

            stmt.executeUpdate();

        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            throw new MarsException("Could not update the user!");
        }
    }

    @Override
    public List<User> getUsers() {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SELECT_ALL_USERS);
             ResultSet rs = stmt.executeQuery()) {

            List<User> users = new ArrayList<>();

            while (rs.next()) {
                users.add(getUserFromResultSet(rs));
            }

            return users;

        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            throw new MarsException("Could not get all users!");
        }
    }

    @Override
    public User getUserViaEmail(String email) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SELECT_USER_VIA_EMAIL)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return getUserFromResultSet(rs);
            }

        } catch (SQLException ex) {
            throw handleCouldNotFindUser(ex);
        }
    }

    @Override
    public int getIdViaEmail(String email) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SELECT_USER_VIA_EMAIL)) {

            stmt.setString(1, email);
            int id;

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                id = rs.getInt("id");
            }

            return id;

        } catch (SQLException ex) {
            throw handleCouldNotFindUser(ex);
        }
    }

    @Override
    public Role getRoleViaEmail(String email) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_GET_ROLE_VIA_EMAIL)) {

            stmt.setString(1, email);

            String roleName;
            int rank;
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();

                roleName = rs.getString("name");
                rank = rs.getInt("rank");
            }

            return new Role(roleName, rank);

        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            throw new MarsException("Could not get the role!");
        }
    }

    private void createUserStatement(PreparedStatement stmt, User user) throws SQLException {
        stmt.setString(1, user.getFirstName());
        stmt.setString(2, user.getLastName());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getPhoneNumber());
        stmt.setString(5, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        stmt.setString(6, user.getAddress().getPlanet());
        stmt.setString(7, user.getAddress().getCountryOrColony());
        stmt.setString(8, user.getAddress().getCityOrDistrict());
        stmt.setString(9, user.getAddress().getStreet());
        stmt.setInt(10, user.getAddress().getNumber());
    }

    private User getUserFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String email = rs.getString("email");
        String phoneNumber = rs.getString("phone_number");
        String userPassword = rs.getString("password");

        Address address = createAddressFromDatabase(rs);

        return new User(id, firstName, lastName, phoneNumber, email, userPassword, address);
    }

    private MarsException handleCouldNotFindUser(Exception ex) {
        logger.log(Level.SEVERE, ex.getMessage());
        return new MarsException("Could not find the user!");
    }

    // Rocket methods:
    @Override
    public List<Rocket> getRockets() {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SELECT_ALL_ROCKETS);
             ResultSet rs = stmt.executeQuery()) {

            List<Rocket> rockets = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String departLocation = rs.getString("depart_location");
                String departure = rs.getString("departure");
                String arrival = rs.getString("arrival");
                int pricePerKilo = rs.getInt("price_per_kilo");
                int maxMass = rs.getInt("max_mass");
                int maxVolume = rs.getInt("max_volume");
                int availableMass = rs.getInt("available_mass");
                int availableVolume = rs.getInt("available_volume");
                rockets.add(new Rocket(id, name, departLocation, departure, arrival, pricePerKilo, maxMass, maxVolume, availableMass, availableVolume));
            }

            return rockets;

        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            throw new MarsException("Could not get rockets!");
        }
    }

    @Override
    public Rocket getRocketById(int rocketId) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SELECT_ROCKET_VIA_ID)) {

            stmt.setInt(1, rocketId);

            try (ResultSet results = stmt.executeQuery()) {
                results.next();

                int id = results.getInt("id");
                String name = results.getString("name");
                String departLocation = results.getString("depart_location");
                String departure = results.getString("departure");
                String arrival = results.getString("arrival");
                float pricePerKilo = results.getFloat("price_per_kilo");
                float maxMass = results.getFloat("max_mass");
                float maxVolume = results.getFloat("max_volume");
                float availableMass = results.getFloat("available_mass");
                float availableVolume = results.getFloat("available_volume");

                return new Rocket(id, name, departLocation, departure, arrival, pricePerKilo, maxMass, maxVolume, availableMass, availableVolume);
            }
        } catch (SQLException ex) {
            throw handleFailedToGetAllOrders(ex);
        }
    }

    @Override
    public void updateRocketAvailableMassAndVolume(int rocketId, float weight, float volume) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_UPDATE_ROCKET)) {

            stmt.setFloat(1, weight);
            stmt.setFloat(2, volume);
            stmt.setDouble(3, rocketId);

            stmt.executeUpdate();

        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            throw new MarsException("Could not update the rocket!");
        }
    }

    @Override
    public Rocket createRocket(Rocket rocket) {
        throw new NotYetBoundException();
    }

    // Order methods:
    @Override
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SELECT_ALL_ORDERS);
             ResultSet results = stmt.executeQuery()) {
            while (results.next()) {
                orders.add(createOrderFromDatabase(results));
            }
        } catch (SQLException ex) {
            throw handleFailedToGetAllOrders(ex);
        }

        return orders;
    }

    @Override
    public Order createOrder(Order order, int userId) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, order.getRocketId());
            stmt.setInt(3, order.getStatusId());
            stmt.setDouble(4, order.getMass());
            stmt.setDouble(5, order.getWidth());
            stmt.setDouble(6, order.getHeight());
            stmt.setDouble(7, order.getDepth());
            stmt.setDouble(8, order.getCost());
            stmt.setString(9, order.getAddress().getPlanet());
            stmt.setString(10, order.getAddress().getCountryOrColony());
            stmt.setString(11, order.getAddress().getCityOrDistrict());
            stmt.setString(12, order.getAddress().getStreet());
            stmt.setInt(13, order.getAddress().getNumber());

            stmt.executeUpdate();

            try (ResultSet rsKey = stmt.getGeneratedKeys()) {
                rsKey.next();

                order.setOrderId(rsKey.getInt(1));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Creating new order failed");
        }

        order.setUserId(userId);

        return order;
    }

    @Override
    public Order getOrderById(int orderId) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SELECT_ORDER_VIA_ID)) {

            stmt.setInt(1, orderId);

            try (ResultSet results = stmt.executeQuery()) {
                results.next();

                return createOrderFromDatabase(results);
            }
        } catch (SQLException ex) {
            throw handleFailedToGetAllOrders(ex);
        }
    }

    @Override
    public List<Order> getOrdersForUser(String email) {
        List<Order> orders = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SELECT_ORDERS_FOR_USER)) {

            stmt.setString(1, email);

            try (ResultSet results = stmt.executeQuery()) {
                while(results.next()) {
                    orders.add(createOrderFromDatabase(results));
                }

                return orders;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new IllegalStateException("Failed to get all orders");
        }
    }

    @Override
    public Map<Integer, String> getIdsForStatuses() {
        Map<Integer, String> statuses = new HashMap<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SELECT_STATUS_ID_AND_NAME);
             ResultSet results = stmt.executeQuery()) {
            while (results.next()) {
                statuses.put(results.getInt("Id"), results.getString("Status"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new IllegalStateException("Failed to get all statuses");
        }

        return statuses;
    }

    private Order createOrderFromDatabase(ResultSet results) {
        try {
            int orderId = results.getInt("id");
            int userId = results.getInt("user_id");
            int rocketId = results.getInt("rocket_id");
            int statusId = results.getInt("status_id");
            float mass = results.getFloat("mass");
            float width = results.getFloat("width");
            float height = results.getFloat("height");
            float depth = results.getFloat("depth");
            double cost = results.getDouble("cost");

            Address address = createAddressFromDatabase(results);

            return new Order(orderId, userId, rocketId, statusId, mass, width, height, depth, cost, address);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            throw new IllegalStateException("Failed to create order from database results");
        }
    }

    private Address createAddressFromDatabase(ResultSet results) {
        try {
            String planet = results.getString("planet");
            String countryOrColony = results.getString("country_or_colony");
            String cityOrDistrict = results.getString("city_or_district");
            String street = results.getString("street");
            int number = results.getInt("number");

            return new Address(planet, countryOrColony, cityOrDistrict, street, number);

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            throw new IllegalStateException("Failed to create address from database results");
        }
    }

    private MarsException handleFailedToGetAllOrders(Exception ex) {
        logger.log(Level.SEVERE, ex.getMessage());
        return new MarsException("Failed to get all orders");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(INSTANCE.url, INSTANCE.username, INSTANCE.password);
    }
}
