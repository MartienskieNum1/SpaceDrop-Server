package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.MarsException;
import org.h2.tools.Server;

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

    public MarsRepository() { }

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

    private final String SQL_INSERT_USER = "insert into Users(first_name, last_name, email, phone_number, password) " +
            "values(?, ?, ?, ?, ?)";
    private final String SQL_SELECT_ALL_USERS = "select * from Users";
    private final String SQL_SELECT_USER_VIA_EMAIL = "select * from Users where email = ?";
    private final String SQL_GET_ROLE_VIA_EMAIL = "select * from roles join userroles on roles.id = userroles.role_id " +
            "join users on userroles.user_id = users.id where email = ?";

    public void createUser(User user) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_INSERT_USER)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getPassword());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Could not create new user!");
        }
    }

    public List<User> getUsers() {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SELECT_ALL_USERS);
             ResultSet rs = stmt.executeQuery()) {

            List<User> users = new ArrayList<>();

            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String userPassword = rs.getString("password");

                users.add(new User(firstName, lastName, email, phoneNumber, userPassword));
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

            ResultSet rs = stmt.executeQuery();
            rs.next();

            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String phoneNumber = rs.getString("phone_number");
            String userPassword = rs.getString("password");

            return new User(firstName, lastName, email, phoneNumber, userPassword);

        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            throw new MarsException("Could not find the user!");
        }
    }

    public Role getRoleViaEmail(String email) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_GET_ROLE_VIA_EMAIL)) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            String roleName = rs.getString("name");
            int rank = rs.getInt("rank");

            return new Role(roleName, rank);

        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            throw new MarsException("Could not get the role!");
        }
    }

    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(INSTANCE.url, INSTANCE.username, INSTANCE.password);
    }
}
