package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.util.MarsException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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
        repo.createOrder(new Order(1, UUID.randomUUID(), 1, 1, 1, "status", 500, 500, 500, 500, 500, address), 1);
        assertEquals(1, repo.getOrders().size());
    }

    @Test
    void getOrderById() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        Order order1 = new Order(1, UUID.randomUUID(), 1, 1, 1, "status", 500, 500, 500, 500, 500, address);
        Order order2 = new Order(5, UUID.randomUUID(), 1, 1, 1, "status", 500, 500, 500, 500, 500, address);
        repo.createOrder(order1, 1);
        repo.createOrder(order2, 1);

        assertEquals(order2, repo.getOrderById(5));
    }

    @Test
    void getOrdersForUser() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user1 = new User(1, "Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);
        Order order1 = new Order(1, UUID.randomUUID(), 1, 1, 1, "status", 500, 500, 500, 500, 500, address);
        Order order2 = new Order(5, UUID.randomUUID(), 1, 1, 1, "status", 500, 500, 500, 500, 500, address);
        Order order3 = new Order(2, UUID.randomUUID(), 2, 1, 1, "status", 500, 500, 500, 500, 500, address);

        repo.createUser(user1);
        repo.createOrder(order1, 1);
        repo.createOrder(order2, 1);
        repo.createOrder(order3, 1);

        assertEquals(2, repo.getOrdersForUser("maarten@maarten").size());
    }

    @Test
    void createRocket() {
        repo.createRocket(new Rocket(10,"Falcon Heavy", "Mars", "2057-06-05 13:30:00", "2057-07-05 08:20:30", 100.0f, 100000.0f, 270.0f, 100000.0f, 270.0f));
        repo.createRocket(new Rocket(20,"Falcon Heavy", "Mars", "2057-06-05 13:30:00", "2057-07-05 08:20:30", 1000.0f, 1000.0f, 2700.0f, 1000.0f, 2700.0f));

        assertEquals(7, repo.getRockets().size());
        assertEquals(100000.0f, repo.getRocketById(10).getMaxMass());
        assertEquals(1000.0f, repo.getRocketById(20).getMaxMass());
    }

    @Test
    void updateRocketAvailableMassAndVolume() {
        createRocket();
        repo.updateRocketAvailableMassAndVolume(1, 150.0f, 200.0f);

        assertEquals(9850.0f, repo.getRocketById(1).getAvailableMass());
        assertEquals(2500.0f, repo.getRocketById(1).getAvailableVolume());

        repo.updateRocketAvailableMassAndVolume(1, 15000000.0f, 200.0f);
        assertEquals(9850.0f, repo.getRocketById(1).getAvailableMass());
        assertEquals(2500.0f, repo.getRocketById(1).getAvailableVolume());
    }

    @Test
    void rocketsJSON() {
        Rocket rocket = repo.getRocketById(1);
        JsonObject rocketAsJsonObject = JsonObject.mapFrom(rocket);

        assertTrue(rocketAsJsonObject.containsKey("id"));
        assertTrue(rocketAsJsonObject.containsKey("name"));
        assertTrue(rocketAsJsonObject.containsKey("departLocation"));
        assertTrue(rocketAsJsonObject.containsKey("departure"));
        assertTrue(rocketAsJsonObject.containsKey("arrival"));
        assertTrue(rocketAsJsonObject.containsKey("pricePerKilo"));
        assertTrue(rocketAsJsonObject.containsKey("maxMass"));
        assertTrue(rocketAsJsonObject.containsKey("maxVolume"));
        assertTrue(rocketAsJsonObject.containsKey("availableMass"));
        assertTrue(rocketAsJsonObject.containsKey("availableVolume"));

        assertEquals(rocket, rocketAsJsonObject.mapTo(Rocket.class));
        assertEquals(rocket, Json.decodeValue(Json.encode(rocket), Rocket.class));
    }

    @Test
    void addressJSON() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        JsonObject addressAsJsonObject = JsonObject.mapFrom(address);

        assertTrue(addressAsJsonObject.containsKey("planet"));
        assertTrue(addressAsJsonObject.containsKey("countryOrColony"));
        assertTrue(addressAsJsonObject.containsKey("cityOrDistrict"));
        assertTrue(addressAsJsonObject.containsKey("street"));
        assertTrue(addressAsJsonObject.containsKey("number"));

        assertEquals(address, addressAsJsonObject.mapTo(Address.class));
        assertEquals(address, Json.decodeValue(Json.encode(address), Address.class));
    }

    @Test
    void ordersJSON() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        Order order = new Order(1, UUID.randomUUID(), 1, 1, 1, "status", 500, 500, 500, 500, 500, address);
        JsonObject orderASJsonObject = JsonObject.mapFrom(order);

        assertTrue(orderASJsonObject.containsKey("orderId"));
        assertTrue(orderASJsonObject.containsKey("uuid"));
        assertTrue(orderASJsonObject.containsKey("userId"));
        assertTrue(orderASJsonObject.containsKey("rocketId"));
        assertTrue(orderASJsonObject.containsKey("statusId"));
        assertTrue(orderASJsonObject.containsKey("status"));
        assertTrue(orderASJsonObject.containsKey("mass"));
        assertTrue(orderASJsonObject.containsKey("width"));
        assertTrue(orderASJsonObject.containsKey("height"));
        assertTrue(orderASJsonObject.containsKey("depth"));
        assertTrue(orderASJsonObject.containsKey("cost"));
        assertTrue(orderASJsonObject.containsKey("address"));

        assertEquals(order, orderASJsonObject.mapTo(Order.class));
        assertEquals(order, Json.decodeValue(Json.encode(order), Order.class));
    }

    @Test
    void rolesJSON() {
        Role role = new Role("User", 2);
        JsonObject roleAsJsonObject = JsonObject.mapFrom(role);

        assertTrue(roleAsJsonObject.containsKey("name"));
        assertTrue(roleAsJsonObject.containsKey("rank"));

        assertEquals(role, roleAsJsonObject.mapTo(Role.class));
        assertEquals(role, Json.decodeValue(Json.encode(role), Role.class));
    }

    @Test
    void usersJSON() {
        Address address = new Address("Earth", "Belgium", "City", "Street", 0);
        User user = new User(1, "Maarten", "De Meyere", "0412345678", "maarten@maarten", "pass", address);
        JsonObject userAsJsonObject = JsonObject.mapFrom(user);

        assertTrue(userAsJsonObject.containsKey("id"));
        assertTrue(userAsJsonObject.containsKey("firstName"));
        assertTrue(userAsJsonObject.containsKey("lastName"));
        assertTrue(userAsJsonObject.containsKey("phoneNumber"));
        assertTrue(userAsJsonObject.containsKey("email"));
        assertTrue(userAsJsonObject.containsKey("password"));
        assertTrue(userAsJsonObject.containsKey("address"));

        assertEquals(user, userAsJsonObject.mapTo(User.class));
        assertEquals(user, Json.decodeValue(Json.encode(user), User.class));
    }
}