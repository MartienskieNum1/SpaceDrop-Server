package be.howest.ti.mars.logic.util;

import be.howest.ti.mars.logic.data.MarsRepository;
import be.howest.ti.mars.logic.data.MockRepository;
import be.howest.ti.mars.logic.domain.Address;
import be.howest.ti.mars.logic.domain.Order;
import be.howest.ti.mars.logic.domain.Rocket;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StatusUpdateTaskTest {
    private final MarsRepository repo = new MockRepository();

    @Test
    void updateStatus() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now().plusYears(35);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        StatusUpdateTask updateTask = new StatusUpdateTask();

        Address address = new Address("Earth", "Belgium", "City", "Street", 1);
        Order order1 = new Order(1, UUID.randomUUID(), 1, 10, 1, "status", 220, 50, 50, 50, 250, address);
        Order order2 = new Order(2, UUID.randomUUID(), 1, 10, 1, "status", 500, 5, 5, 5, 50, address);
        Rocket rocket = new Rocket(10, "ultra fast rocket", "Mars", now.plusSeconds(1).format(formatter), now.plusSeconds(2).format(formatter), 100.0f, 10000.0f, 2700.0f, 10000.0f, 2700.0f);

        repo.createOrder(order1, 1);
        repo.createOrder(order2, 1);
        repo.createRocket(rocket);

        updateTask.updateStatus(repo);
        assertEquals(1, repo.getOrderById(order1.getOrderId()).getStatusId());
        assertEquals(1, repo.getOrderById(order2.getOrderId()).getStatusId());

        Thread.sleep(1050);

        updateTask.updateStatus(repo);
        assertEquals(2, repo.getOrderById(order1.getOrderId()).getStatusId());
        assertEquals(2, repo.getOrderById(order2.getOrderId()).getStatusId());

        Thread.sleep(2050);

        updateTask.updateStatus(repo);
        assertEquals(3, repo.getOrderById(order1.getOrderId()).getStatusId());
        assertEquals(3, repo.getOrderById(order2.getOrderId()).getStatusId());
    }
}