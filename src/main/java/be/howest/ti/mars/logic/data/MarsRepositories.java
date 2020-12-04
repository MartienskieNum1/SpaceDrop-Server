package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Order;
import be.howest.ti.mars.logic.domain.Rocket;
import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;

import java.util.List;
import java.util.Map;

public interface MarsRepositories {

    // User methods:
    void createUser(User user);
    void setUser(User ogUser, User moddedUser);
    List<User> getUsers();
    User getUserViaEmail(String email);
    int getIdViaEmail(String email);
    Role getRoleViaEmail(String email);

    // Rocket methods:
    List<Rocket> getRockets();

    // Order methods:
    List<Order> getOrders();
    Order createOrder(Order order);
    Order getOrderById(int orderId);
    List<Order> getOrdersForUser(String email);
    Map<Integer, String> getIdsForStatuses();

}
