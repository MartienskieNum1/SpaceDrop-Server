package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Order;
import be.howest.ti.mars.logic.domain.Rocket;
import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.MarsException;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.channels.NotYetBoundException;
import java.util.*;

public class MockRepository implements MarsRepository {
    private static final MockRepository INSTANCE = new MockRepository();

    private Map<User, Role> userRoleMap = new HashMap<>();
    private Set<Order> orders = new HashSet<>();
    private Set<Rocket> rockets = new HashSet<>();
    private Set<Role> roles = new HashSet<>();

    public static MockRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void createUser(User user) {
        Role role = new Role("User", 2);
        User hashedUser = new User(user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getEmail(),
                BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()), user.getAddress());
        if (!userExists(user)) {
            userRoleMap.put(hashedUser, role);
        } else {
            throw new MarsException("Could not create new user!");
        }
    }

    @Override
    public void setUser(User ogUser, User moddedUser) {
        Role role = new Role("User", 2);
        if (!userExists(moddedUser)) {
            userRoleMap.remove(ogUser);
            userRoleMap.put(moddedUser, role);
        } else {
            throw new MarsException("Could not update the user!");
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(userRoleMap.keySet());
    }

    @Override
    public User getUserViaEmail(String email) {
        for (Map.Entry<User, Role> entry : userRoleMap.entrySet()) {
            if (entry.getKey().getEmail().equals(email))
                return entry.getKey();
        }
        throw new MarsException("Could not find the user!");
    }

    @Override
    public int getIdViaEmail(String email) {
        for (Map.Entry<User, Role> entry : userRoleMap.entrySet()) {
            if (entry.getKey().getEmail().equals(email))
                return entry.getKey().getId();
        }
        throw new MarsException("Could not find the user!");
    }

    @Override
    public Role getRoleViaEmail(String email) {
        for (Map.Entry<User, Role> entry : userRoleMap.entrySet()) {
            if (entry.getKey().getEmail().equals(email))
                return entry.getValue();
        }
        throw new MarsException("Could not find the user!");
    }

    private boolean userExists(User user) {
        return userRoleMap.containsKey(user);
    }

    @Override
    public List<Rocket> getRockets() {
        return new ArrayList<>(rockets);
    }

    public void addRocket(Rocket rocket) {
        rockets.add(rocket);
    }

    @Override
    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    @Override
    public Order createOrder(Order order) {
        orders.add(order);
        return order;
    }

    @Override
    public Order getOrderById(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId)
                return order;
        }
        throw new IllegalStateException("Failed to get all orders");
    }

    @Override
    public List<Order> getOrdersForUser(String email) {
        int userId = getUserViaEmail(email).getId();

        List<Order> userOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getUserId() == userId) {
                userOrders.add(order);
            }
        }

        return userOrders;
    }

    @Override
    public Map<Integer, String> getIdsForStatuses() {
        throw new NotYetBoundException();
    }
}
