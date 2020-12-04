package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Order;
import be.howest.ti.mars.logic.domain.Rocket;
import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;

import java.util.List;
import java.util.Map;

public class MockRepository implements MarsRepository {
    @Override
    public void createUser(User user) {

    }

    @Override
    public void setUser(User ogUser, User moddedUser) {

    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public User getUserViaEmail(String email) {
        return null;
    }

    @Override
    public int getIdViaEmail(String email) {
        return 0;
    }

    @Override
    public Role getRoleViaEmail(String email) {
        return null;
    }

    @Override
    public List<Rocket> getRockets() {
        return null;
    }

    @Override
    public List<Order> getOrders() {
        return null;
    }

    @Override
    public Order createOrder(Order order) {
        return null;
    }

    @Override
    public Order getOrderById(int orderId) {
        return null;
    }

    @Override
    public List<Order> getOrdersForUser(String email) {
        return null;
    }

    @Override
    public Map<Integer, String> getIdsForStatuses() {
        return null;
    }
}
