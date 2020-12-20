package be.howest.ti.mars.logic.util;

import be.howest.ti.mars.logic.data.MarsRepository;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Order;
import be.howest.ti.mars.logic.domain.Rocket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimerTask;

public class StatusUpdateTask extends TimerTask {
    private static final MarsRepository h2Repo = Repositories.H2REPO;

    @Override
    public void run() {
        updateStatus(h2Repo);
    }

    public void updateStatus(MarsRepository repo) {
        List<Order> orders = repo.getOrders();

        orders.forEach(order -> {
            int orderId = order.getOrderId();
            int currentStatusId = order.getStatusId();

            Rocket rocket = repo.getRocketById(order.getRocketId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now().plusYears(35);
            LocalDateTime departure = LocalDateTime.parse(rocket.getDeparture(), formatter);
            LocalDateTime arrival = LocalDateTime.parse(rocket.getArrival(), formatter);

            if (now.isBefore(departure) && currentStatusId != 1) {
                repo.updateOrderStatus(orderId, 1);
            } else if (now.isAfter(departure) && now.isBefore(arrival) && currentStatusId != 2) {
                repo.updateOrderStatus(orderId, 2);
            } else if (now.isAfter(arrival) && currentStatusId != 3) {
                repo.updateOrderStatus(orderId, 3);
            }
        });
    }
}
