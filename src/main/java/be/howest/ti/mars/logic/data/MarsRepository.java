package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Address;
import be.howest.ti.mars.logic.domain.Rocket;
import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.MarsException;
import org.h2.tools.Server;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
public class MarsRepository {
    private static final MarsRepository INSTANCE = new MarsRepository();
    private final Logger LOGGER = Logger.getLogger(MarsRepository.class.getName());

    private Server dbWebConsole;
    private String username;
    private String password;
    private String url;

    public MarsRepository() {
        // Should not be called
    }

    public static MarsRepository getInstance() {
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
    private final String SQL_UPDATE_USER = "update users set first_name = ?, last_name = ?, email = ?, phone_number = ?, " +
            "password = ?, planet = ?, country_or_colony = ?, city_or_district = ?, street = ?, number = ? where email = ?";
    private final String SQL_BIND_ROLE_TO_USER = "insert into userroles(user_id, role_id) values(?, ?)";
    private final String SQL_SELECT_ALL_USERS = "select * from Users";
    private final String SQL_SELECT_USER_VIA_EMAIL = "select * from Users where email = ?";
    private final String SQL_GET_ROLE_VIA_EMAIL = "select * from roles join userroles on roles.id = userroles.role_id " +
            "join users on userroles.user_id = users.id where email = ?";
    private final String SQL_SELECT_ALL_ROCKETS = "select * from rockets";

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

        return new User(firstName, lastName, phoneNumber, email, userPassword, address);
    }

    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(INSTANCE.url, INSTANCE.username, INSTANCE.password);
    }

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
}
