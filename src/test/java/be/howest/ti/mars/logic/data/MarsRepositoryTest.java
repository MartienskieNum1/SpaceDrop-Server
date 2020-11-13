package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.util.MarsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MarsRepositoryTest {
    private final Logger LOGGER = Logger.getLogger(MarsRepository.class.getName());
    private static final String URL = "jdbc:h2:~/mars-db";

    @BeforeAll
    void setupTestSuite() throws SQLException {
        MarsRepository.configure(URL, "", "", 9000);
    }

    @BeforeEach
    void setupTest() throws IOException {
        createDatabase();
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
