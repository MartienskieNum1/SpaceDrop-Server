package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Order;
import be.howest.ti.mars.logic.domain.Rocket;
import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MarsRepository {

    // User methods:
    void createUser(User user);
    void setUser(User ogUser, User moddedUser);
    List<User> getUsers();
    User getUserViaEmail(String email);
    int getIdViaEmail(String email);
    Role getRoleViaEmail(String email);

    // Rocket methods:
    List<Rocket> getRockets();
    Rocket getRocketById(int rocketId);
    void updateRocketAvailableMassAndVolume(int rocketId, float weight, float volume);
    List<Rocket> getFilteredRockets(float weight, float volume, LocalDateTime[] dates);
    Rocket createRocket(Rocket rocket);

    // Order methods:
    List<Order> getOrders();
    Order createOrder(Order order, int userId);
    Order getOrderById(int orderId);
    Order getOrderByUuid(UUID uuid);
    List<Order> getOrdersForUser(String email);
    void updateOrderStatus(int orderId, int statusId);

    // Status methods:
    Map<Integer, String> getIdsForStatuses();
}
