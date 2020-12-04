package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.util.MarsException;
import org.h2.tools.Server;
import org.mindrot.jbcrypt.BCrypt;

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
    private final Logger LOGGER = Logger.getLogger(H2Repository.class.getName());

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

    private final String SQL_INSERT_USER = "insert into Users(first_name, last_name, email, phone_number, password, planet, country_or_colony, city_or_district, street, number) " +
            "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQL_BIND_ROLE_TO_USER = "insert into userroles(user_id, role_id) values(?, ?)";
    private final String SQL_UPDATE_USER = "update users set first_name = ?, last_name = ?, email = ?, phone_number = ?, " +
            "password = ?, planet = ?, country_or_colony = ?, city_or_district = ?, street = ?, number = ? where email = ?";
    private final String SQL_SELECT_ALL_USERS = "select * from Users";
    private final String SQL_SELECT_USER_VIA_EMAIL = "select * from Users where email = ?";
    private final String SQL_GET_ROLE_VIA_EMAIL = "select * from roles join userroles on roles.id = userroles.role_id " +
            "join users on userroles.user_id = users.id where email = ?";

    private final String SQL_SELECT_ALL_ROCKETS = "select * from rockets";

    private final String SQL_SELECT_ALL_ORDERS = "select * from orders";
    private final String SQL_INSERT_ORDER = "insert into Orders(user_id, rocket_id, status_id, mass, width, height, depth, cost) " +
            "values(?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQL_SELECT_ORDER_VIA_ID = "select * from orders where id = ?";
    private final String SQL_SELECT_ORDERS_FOR_USER = "select * from orders where user_id = (select id from users where email = ?)";
    private final String SQL_SELECT_STATUS_ID_AND_NAME = "select * from statuses";

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
            LOGGER.log(Level.SEVERE, ex.getMessage());
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
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Could not bind user to role!");
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
            LOGGER.log(Level.WARNING, ex.getMessage());
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
            LOGGER.log(Level.WARNING, ex.getMessage());
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
            LOGGER.log(Level.WARNING, ex.getMessage());
            throw new MarsException("Could not find the user!");
        }
    }

    @Override
    public int getIdViaEmail(String email) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SELECT_USER_VIA_EMAIL)) {

            stmt.setString(1, email);
            int id = -1;

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                id = rs.getInt("id");
            }

            return id;

        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            throw new MarsException("Could not find the user!");
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
            LOGGER.log(Level.WARNING, ex.getMessage());
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

        String planet = rs.getString("planet");
        String countryOrColony = rs.getString("country_or_colony");
        String cityOrDistrict = rs.getString("city_or_district");
        String street = rs.getString("street");
        int number = rs.getInt("number");

        Address address = new Address(planet, countryOrColony, cityOrDistrict, street, number);

        return new User(id, firstName, lastName, phoneNumber, email, userPassword, address);
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
            LOGGER.log(Level.WARNING, ex.getMessage());
            throw new MarsException("Could not get rockets!");
        }
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
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new IllegalStateException("Failed to get all orders");
        }

        return orders;
    }

    @Override
    public Order createOrder(Order order) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getUserId());
            stmt.setInt(2, order.getRocketId());
            stmt.setInt(3, order.getStatusId());
            stmt.setDouble(4, order.getMass());
            stmt.setDouble(5, order.getWidth());
            stmt.setDouble(6, order.getHeight());
            stmt.setDouble(7, order.getDepth());
            stmt.setDouble(8, order.getCost());

            stmt.executeUpdate();

            try (ResultSet rsKey = stmt.getGeneratedKeys()) {
                rsKey.next();

                order.setOrderId(rsKey.getInt(1));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Creating new order failed");
        }

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
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new IllegalStateException("Failed to get all orders");
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
                    System.out.println(results);
                }

                return orders;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
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
            LOGGER.log(Level.SEVERE, e.getMessage());
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
            double mass = results.getDouble("mass");
            double width = results.getDouble("width");
            double height = results.getDouble("height");
            double depth = results.getDouble("depth");
            double cost = results.getDouble("cost");

            return new Order(orderId, userId, rocketId, statusId, mass, width, height, depth, cost);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new IllegalStateException("Failed to create order from database results");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(INSTANCE.url, INSTANCE.username, INSTANCE.password);
    }
}
