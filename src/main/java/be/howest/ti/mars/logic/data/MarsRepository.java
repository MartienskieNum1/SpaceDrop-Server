package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.MarsException;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private static final Logger LOGGER = Logger.getLogger(MarsRepository.class.getName());

    private Server dbWebConsole;
    private String username;
    private String password;
    private String url;

    private MarsRepository() { }

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

    private static final String SQL_INSERT_USER = "insert into Users(first_name, last_name, email, phone_number, password) " +
            "values(?, ?, ?, ?, ?)";

    public static void createUser(User user) {
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
            throw new MarsException("User was not created!");
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(INSTANCE.url, INSTANCE.username, INSTANCE.password);
    }
}
