package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Order;

import java.util.List;
import java.util.Map;

public interface OrderRepository {
    List<Order> getOrders();
    Order createOrder(Order order);
    Order getOrderById(int orderId);
    List<Order> getOrdersForUser(String email);
    Map<Integer, String> getIdsForStatuses();
}
