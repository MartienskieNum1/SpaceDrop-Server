package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.MockRepository;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.util.TokenAES;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

    MarsController controller = new MarsController(new MockRepository());
    User generalUser;

    @BeforeEach
    void populate() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user1 = new User("Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);
        generalUser = user1;

        controller.createUser(user1);

        new Rocket(1,"Falcon Heavy", "Mars", "2057-06-05 13:30:00", "2057-07-05 08:20:30", 100.0f, 100000.0f, 270.0f, 100000.0f, 270.0f);
        new Rocket(2,"Falcon Heavy", "Mars", "2057-06-05 13:30:00", "2057-07-05 08:20:30", 1000.0f, 1000.0f, 2700.0f, 1000.0f, 2700.0f);
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
    void successfulSetUser() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user = new User("Mira", "Vogelsang", "0412345678", "mira@mira", "pass", address);

        assertEquals(TokenAES.encrypt(user.getEmail()), controller.setUser(generalUser.getEmail(), generalUser.getPassword(), user));
    }

    @Test
    void wrongPassSetUser() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user = new User("Mira", "Vogelsang", "0412345678", "mira@mira", "pass", address);

        assertNull(controller.setUser(generalUser.getEmail(), BCrypt.hashpw("rand", BCrypt.gensalt()), user));
    }

    @Test
    void emailInUseSetUser() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user = new User("Mira", "Vogelsang", "0412345678", "maarten@maarten", "pass", address);

        assertNull(controller.setUser(generalUser.getEmail(), BCrypt.hashpw("rand", BCrypt.gensalt()), user));
    }

    @Test
    void userDoesExist() {
        assertTrue(controller.userExists(generalUser.getEmail()));
    }

    @Test
    void userDoesNotExist() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user = new User("Mira", "Vogelsang", "0412345678", "mira@mira", "pass", address);

        assertFalse(controller.userExists(user.getEmail()));
    }

    @Test
    void getRoleViaEmail() {
        Role role = new Role("User", 2);
        assertEquals(role, controller.getRoleViaEmail(generalUser.getEmail()));
    }

    @Test
    void successfulGetUser() {
        assertEquals(generalUser, controller.getUser(generalUser.getEmail()));
    }

    @Test
    void wrongEmailGetUSer() {
        assertNull(controller.getUser("random@random"));
    }

    @Test
    void successfulCreateOrder() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        Order order = new Order(1, UUID.randomUUID(), 1, 1, 1, "status", 1, 1, 1, 1, 1, address);
        assertEquals(order, controller.createOrder(order, 1));
    }

    @Test
    void departedCreateOrder() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        Order order = new Order(1, UUID.randomUUID(), 1, 4, 1, "status", 1, 1, 1, 1, 1, address);

        assertNull(controller.createOrder(order, 1));
    }

    @Test
    void successfulGetOrderById() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        Order order1 = new Order(1, UUID.randomUUID(), 1, 1, 1, "status", 1, 1, 1, 1, 1, address);
        Order order2 = new Order(2, UUID.randomUUID(), 1, 1, 1, "status", 1, 1, 1, 1, 1, address);

        controller.createOrder(order1, 1);
        controller.createOrder(order2, 1);

        assertEquals(order2, controller.getOrderById(2));
    }

    @Test
    void wrongIdGetOrderById() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        Order order1 = new Order(1, UUID.randomUUID(), 1, 1, 1, "status", 1, 1, 1, 1, 1, address);
        Order order2 = new Order(2, UUID.randomUUID(), 1, 1, 1, "status", 1, 1, 1, 1, 1, address);

        controller.createOrder(order1, 1);
        controller.createOrder(order2, 1);

        assertNull(controller.getOrderById(99));
    }

    @Test
    void successfulGetUserId() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user = new User(15, "Mira", "Vogelsang", "0412345678", "mira@mira", "pass", address);

        controller.createUser(user);

        assertEquals(15, controller.getUserId(user.getEmail()));
    }

    @Test
    void wrongIdGetUserId() {
        assertEquals(-1, controller.getUserId("random@random"));
    }

    @Test
    void successfulGetOrdersForUser() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user = new User(15, "Mira", "Vogelsang", "0412345678", "mira@mira", "pass", address);
        Order order1 = new Order(100, UUID.randomUUID(), 15, 1, 1, "status", 1, 1, 1, 1, 1, address);
        Order order2 = new Order(102, UUID.randomUUID(), 15, 1, 1, "status", 1, 1, 1, 1, 1, address);
        List<Order> orders = Arrays.asList(order1, order2);

        controller.createUser(user);
        controller.createOrder(order1, user.getId());
        controller.createOrder(order2, user.getId());

        assertEquals(orders, controller.getOrdersForUser(user.getEmail()));
    }

    @Test
    void testGetFilteredRockets() {
        assertEquals(5, controller.getFilteredFlights(50, 50, "fast").size());
        assertEquals(1, controller.getFilteredFlights(3000, 3000, "fast").size());
        assertEquals(3, controller.getFilteredFlights(50, 1150, "fast").size());
    }

    @Test
    void wrongEmailGetOrdersForUser() {
        assertEquals(Collections.emptyList(), controller.getOrdersForUser("random@random"));
    }
}
