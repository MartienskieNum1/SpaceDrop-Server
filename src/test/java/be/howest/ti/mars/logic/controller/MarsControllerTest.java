package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Address;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.TokenAES;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

class MarsControllerTest {

    @Test
    void getMessageReturnsAWelcomeMessage() {
        // Arrange
        MarsController sut = new MarsController(Repositories.H2REPO);

        // Act
        String message = sut.getMessage();

        //Assert
        assertTrue(StringUtils.isNoneBlank(message));
    }

    MarsController controller = new MarsController(Repositories.MOCK_REPO);
    User generalUser;

    @BeforeEach
    void populate() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user1 = new User("Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);
        generalUser = user1;

        controller.createUser(user1);
    }

    @Test
    void createUserReturnsToken() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user = new User("Maarten", "De Meyere", "0412345678", "maarten1@maarten1", "pass", address);

        assertEquals(TokenAES.encrypt(user.getEmail()), controller.createUser(user));
    }

    @Test
    void createUserReturnsNull() { //email is same of generalUser
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user = new User("Mira", "Vogelsang", "0412345678", "maarten@maarten", "pass", address);

        assertNull(controller.createUser(user));
    }

    @Test
    void successfulLogin() {
        assertEquals(TokenAES.encrypt(generalUser.getEmail()), controller.login(generalUser.getEmail(), generalUser.getPassword()));
    }

    @Test
    void wrongPassLogin() {
        assertNull(controller.login(generalUser.getEmail(), BCrypt.hashpw("randomPass", BCrypt.gensalt())));
    }

    @Test
    void wrongEmailLogin() {
        assertNull(controller.login("Random@Email", generalUser.getPassword()));
    }

    @Test
    void setUser() {
    }

    @Test
    void userExists() {
    }

    @Test
    void getRoleViaEmail() {
    }

    @Test
    void getUsers() {
    }

    @Test
    void getUser() {
    }

    @Test
    void getOrders() {
    }

    @Test
    void createOrder() {
    }

    @Test
    void getOrderById() {
    }

    @Test
    void getRockets() {
    }

    @Test
    void getUserId() {
    }

    @Test
    void getOrdersForUser() {
    }

    @Test
    void getIdsForStatuses() {
    }
}
