package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Order;
import be.howest.ti.mars.logic.domain.Rocket;
import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.MarsException;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.channels.NotYetBoundException;
import java.time.LocalDateTime;
import java.util.*;

public class MockRepository implements MarsRepository {
    private static final MockRepository INSTANCE = new MockRepository();

    private final Map<User, Role> userRoleMap = new HashMap<>();
    private final Set<Order> orders = new HashSet<>();
    private final Set<Rocket> rockets = new HashSet<>();

    public static MockRepository getInstance() {
        return INSTANCE;
    }

    private static final String COULD_NOT_FIND_THE_USER = "Could not find the user!";
    private static final String COULD_NOT_FIND_THE_ORDERS = "Failed to get all orders";

    {
        createRocket(new Rocket(1,"Falcon Heavy", "Mars", "2057-06-05 13:30:00", "2057-07-05 08:20:30", 100.0f, 100000.0f, 270.0f, 100000.0f, 270.0f));
        createRocket(new Rocket(2, "departed rocket", "Mars", "2000-01-01 12:00:00", "2000-04-01 12:00:00", 100, 100000, 270, 100000, 270));
    }

    @Override
    public void createUser(User user) {
        Role role = new Role("User", 2);
        User hashedUser;
        if (user.getId() == -1) {
            hashedUser = new User(user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getEmail(),
                    BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()), user.getAddress());
        } else {
            hashedUser = new User(user.getId(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getEmail(),
                    BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()), user.getAddress());
        }
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
        throw new MarsException(COULD_NOT_FIND_THE_USER);
    }

    @Override
    public int getIdViaEmail(String email) {
        for (Map.Entry<User, Role> entry : userRoleMap.entrySet()) {
            if (entry.getKey().getEmail().equals(email))
                return entry.getKey().getId();
        }
        throw new MarsException(COULD_NOT_FIND_THE_USER);
    }

    @Override
    public Role getRoleViaEmail(String email) {
        for (Map.Entry<User, Role> entry : userRoleMap.entrySet()) {
            if (entry.getKey().getEmail().equals(email))
                return entry.getValue();
        }
        throw new MarsException(COULD_NOT_FIND_THE_USER);
    }

    private boolean userExists(User user) {
        return userRoleMap.containsKey(user);
    }

    @Override
    public List<Rocket> getRockets() {
        return new ArrayList<>(rockets);
    }

    @Override
    public Rocket getRocketById(int rocketId) {
        for (Rocket rocket : rockets) {
            if (rocket.getId() == rocketId)
                return rocket;
        }
        throw new IllegalStateException("Failed to find rocket");
    }

    public Rocket createRocket(Rocket rocket) {
        rockets.add(rocket);

        return rocket;
    }

    @Override
    public void updateRocketAvailableMassAndVolume(int rocketId, float weight, float volume) {
        Rocket targetRocket = null;

        for (Rocket rocket : rockets) {
            if (rocket.getId() == rocketId)
                targetRocket = rocket;
        }

        if (targetRocket != null) {
            if (targetRocket.getAvailableMass() - weight > 0 && targetRocket.getAvailableVolume() - volume > 0) {
                targetRocket.setAvailableMass(targetRocket.getAvailableMass() - weight);
                targetRocket.setAvailableVolume(targetRocket.getAvailableVolume() - volume);
            }
        }
    }

    @Override
    public List<Rocket> getFilteredRockets(float weight, float volume, LocalDateTime[] dates) {
        return null;
    }

    @Override
    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    @Override
    public Order createOrder(Order order, int userId) {
        orders.add(order);
        return order;
    }

    @Override
    public Order getOrderById(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId)
                return order;
        }
        throw new IllegalStateException(COULD_NOT_FIND_THE_ORDERS);
    }

    @Override
    public Order getOrderByUuid(UUID uuid) {
        for (Order order : orders) {
            if (order.getUuid() == uuid)
                return order;
        }
        throw new IllegalStateException(COULD_NOT_FIND_THE_ORDERS);
    }

    @Override
    public List<Order> getOrdersForUser(String email) {
        try {
            int userId = getUserViaEmail(email).getId();

            List<Order> userOrders = new ArrayList<>();
            for (Order order : orders) {
                if (order.getUserId() == userId) {
                    userOrders.add(order);
                }
            }

            return userOrders;
        } catch (MarsException ex) {
            throw new IllegalStateException(COULD_NOT_FIND_THE_ORDERS);
        }
    }

    @Override
    public Map<Integer, String> getIdsForStatuses() {
        throw new NotYetBoundException();
    }
}
