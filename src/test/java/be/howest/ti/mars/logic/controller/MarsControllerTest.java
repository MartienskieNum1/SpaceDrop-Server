package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.MockRepository;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Address;
import be.howest.ti.mars.logic.domain.Order;
import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.TokenAES;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    void createOrder() {
        Order order = new Order(1, 1, 1, 1, 1, 1, 1, 1, 1);
        assertEquals(order, controller.createOrder(order));
    }

    @Test
    void successfulGetOrderById() {
        Order order1 = new Order(1, 1, 1, 1, 1, 1, 1, 1, 1);
        Order order2 = new Order(2, 1, 1, 1, 1, 1, 1, 1, 1);

        controller.createOrder(order1);
        controller.createOrder(order2);

        assertEquals(order2, controller.getOrderById(2));
    }

    @Test
    void wrongIdGetOrderById() {
        Order order1 = new Order(1, 1, 1, 1, 1, 1, 1, 1, 1);
        Order order2 = new Order(2, 1, 1, 1, 1, 1, 1, 1, 1);

        controller.createOrder(order1);
        controller.createOrder(order2);

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
        Order order1 = new Order(1, 15, 1, 1, 1, 1, 1, 1, 1);
        Order order2 = new Order(2, 15, 1, 1, 1, 1, 1, 1, 1);
        List<Order> orders = Arrays.asList(order1, order2);

        controller.createUser(user);
        controller.createOrder(order1);
        controller.createOrder(order2);

        assertEquals(orders, controller.getOrdersForUser(user.getEmail()));
    }

    @Test
    void wrongEmailGetOrdersForUser() {
        assertEquals(Collections.emptyList(), controller.getOrdersForUser("random@random"));
    }
}
