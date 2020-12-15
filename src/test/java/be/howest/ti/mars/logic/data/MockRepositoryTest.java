package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.util.MarsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockRepositoryTest {
    private final MarsRepository repo = new MockRepository();

    @Test
    void createUser() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user = new User("Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);

        repo.createUser(user);

        assertEquals(1, repo.getUsers().size());
    }

    @Test
    void userGetsRoleWhenCreated() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user = new User("Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);
        Role role = new Role("User", 2);

        repo.createUser(user);

        assertEquals(role, repo.getRoleViaEmail("maarten@maarten"));
    }

    @Test
    void setUser() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User ogUser = new User("Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);
        User modUser = new User("Mira", "Vogelsang", "0412345678", "mira@mira", "pass", address);
        repo.createUser(ogUser);

        repo.setUser(ogUser, modUser);

        assertTrue(repo.getUsers().contains(modUser));
        assertThrows(MarsException.class, () -> repo.getUserViaEmail("maarten@maarten"));
    }

    @Test
    void getUserViaEmail() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user1 = new User("Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);
        User user2 = new User("Mira", "Vogelsang", "0412345678", "mira@mira", "pass", address);
        repo.createUser(user1);
        repo.createUser(user2);

        assertEquals(user2, repo.getUserViaEmail("mira@mira"));
    }

    @Test
    void getIdViaEmail() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user1 = new User("Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);
        User user2 = new User("Mira", "Vogelsang", "0412345678", "mira@mira", "pass", address);
        repo.createUser(user1);
        repo.createUser(user2);

        assertEquals(user2.getId(), repo.getIdViaEmail("mira@mira"));
    }

    @Test
    void getRoleViaEmail() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user1 = new User("Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);
        repo.createUser(user1);

        assertEquals(new Role("User", 2), repo.getRoleViaEmail("maarten@maarten"));
    }

    @Test
    void createOrder() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        repo.createOrder(new Order(1, 1, 1, 1, 500, 500, 500, 500, 500, address), 1);
        assertEquals(1, repo.getOrders().size());
    }

    @Test
    void getOrderById() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        Order order1 = new Order(1, 1, 1, 1, 500, 500, 500, 500, 500, address);
        Order order2 = new Order(5, 1, 1, 1, 500, 500, 500, 500, 500, address);
        repo.createOrder(order1, 1);
        repo.createOrder(order2, 1);

        assertEquals(order2, repo.getOrderById(5));
    }

    @Test
    void getOrdersForUser() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user1 = new User(1, "Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);
        Order order1 = new Order(1, 1, 1, 1, 500, 500, 500, 500, 500, address);
        Order order2 = new Order(5, 1, 1, 1, 500, 500, 500, 500, 500, address);
        Order order3 = new Order(2, 2, 1, 1, 500, 500, 500, 500, 500, address);

        repo.createUser(user1);
        repo.createOrder(order1, 1);
        repo.createOrder(order2, 1);
        repo.createOrder(order3, 1);

        assertEquals(2, repo.getOrdersForUser("maarten@maarten").size());
    }

    @Test
    void createRocket() {
        repo.createRocket(new Rocket(1,"Falcon Heavy", "Mars", "2057-06-05 13:30:00", "2057-07-05 08:20:30", 100.0f, 100000.0f, 270.0f, 100000.0f, 270.0f));
        repo.createRocket(new Rocket(2,"Falcon Heavy", "Mars", "2057-06-05 13:30:00", "2057-07-05 08:20:30", 1000.0f, 1000.0f, 2700.0f, 1000.0f, 2700.0f));

        assertEquals(2, repo.getRockets().size());
        assertEquals(100000.0f, repo.getRocketById(1).getMaxMass());
        assertEquals(1000.0f, repo.getRocketById(2).getMaxMass());
    }

    @Test
    void updateRocketAvailableMassAndVolume() {
        createRocket();
        repo.updateRocketAvailableMassAndVolume(1, 150.0f, 200.0f);

        assertEquals(99850.0f, repo.getRocketById(1).getAvailableMass());
        assertEquals(70.0f, repo.getRocketById(1).getAvailableVolume());

        repo.updateRocketAvailableMassAndVolume(1, 15000000.0f, 200.0f);
        assertEquals(99850.0f, repo.getRocketById(1).getAvailableMass());
        assertEquals(70.0f, repo.getRocketById(1).getAvailableVolume());
    }
}