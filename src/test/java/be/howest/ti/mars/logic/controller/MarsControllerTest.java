package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Address;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.TokenAES;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

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

    @Test
    void createUserReturnsToken() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user = new User("Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);

        assertEquals(TokenAES.encrypt(user.getEmail()), controller.createUser(user));
    }

    @Test
    void createUserReturnsNull() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user1 = new User("Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);
        User user2 = new User("Mira", "Vogelsang", "0412345678", "maarten@maarten", "pass", address);

        controller.createUser(user1);

        assertNull(controller.createUser(user2));
    }

    @Test
    void successfulLogin() {
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
