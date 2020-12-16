package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.MarsRepository;
import be.howest.ti.mars.logic.domain.Rocket;
import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.MarsException;
import be.howest.ti.mars.logic.util.TokenAES;
import be.howest.ti.mars.logic.domain.Order;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MarsController {
    private final MarsRepository repo;

    public MarsController(MarsRepository repo) {
        this.repo = repo;
    }

    private static final String MESSAGE = "Hello, Mars!";
    public String getMessage() {
        return MESSAGE;
    }

    public String createUser(User user) {
        try {
            repo.createUser(user);
            return TokenAES.encrypt(user.getEmail());
        } catch (MarsException ex) {
            return null;
        }
    }

    public String login(String email, String password) {
        try {
            User user = repo.getUserViaEmail(email);
            if (BCrypt.checkpw(password, user.getPassword()))
                return TokenAES.encrypt(email);
            return null;
        } catch (MarsException ex) {
            return null;
        }
    }

    public String setUser(String email, String oldPassword, User moddedUser) {
        try {
            User ogUser = repo.getUserViaEmail(email);
            if (BCrypt.checkpw(oldPassword, ogUser.getPassword())) {
                repo.setUser(ogUser, moddedUser);
                return TokenAES.encrypt(moddedUser.getEmail());
            } else {
                return null;
            }
        } catch (MarsException ex) {
            return null;
        }
    }

    public boolean userExists(String email) {
        try {
            repo.getUserViaEmail(email);
            return true;
        } catch (MarsException ex) {
            return false;
        }
    }

    public Role getRoleViaEmail(String email) {
        return repo.getRoleViaEmail(email);
    }

    public List<User> getUsers() {
        return new ArrayList<>(repo.getUsers());
    }

    public User getUser(String email) {
        try {
            return repo.getUserViaEmail(email);
        } catch (MarsException ex) {
            return null;
        }
    }

    public List<Order> getOrders() {
        return new ArrayList<>(repo.getOrders());
    }

    public Order createOrder(Order newOrder, int userId) {
        if (isSpaceOnRocket(newOrder)) {
            updateRocketAvailableMassAndVolume(newOrder);
            return repo.createOrder(newOrder, userId);
        } else {
            return null;
        }
    }

    private void updateRocketAvailableMassAndVolume(Order order) {
        Rocket rocket = repo.getRocketById(order.getRocketId());
        float newMass = rocket.getAvailableMass() - order.getMass();
        float newVolume = rocket.getAvailableVolume() - order.calculateVolume();

        repo.updateRocketAvailableMassAndVolume(rocket.getId(), newMass, newVolume);
    }

    public Order getOrderById(int orderId) {
        try {
            return repo.getOrderById(orderId);
        } catch (IllegalStateException ex) {
            return null;
        }
    }

    public Order getOrderByUuid(UUID uuid) {
        try {
            return repo.getOrderByUuid(uuid);
        } catch (MarsException ex) {
            return null;
        }
    }

    public Object getRockets() {
        return repo.getRockets();
    }

    public int getUserId(String email) {
        try {
            return repo.getIdViaEmail(email);
        } catch (MarsException ex) {
            return -1;
        }
    }

    public List<Order> getOrdersForUser(String email) {
        try {
            return repo.getOrdersForUser(email);
        } catch (IllegalStateException ex) {
            return Collections.emptyList();
        }
    }

    public Map<Integer, String> getIdsForStatuses() {
        return repo.getIdsForStatuses();
    }

    private boolean isSpaceOnRocket(Order order) {
        Rocket rocket = repo.getRocketById(order.getRocketId());

        if (rocket.getAvailableVolume() - order.calculateVolume() >= 0) {
            return rocket.getAvailableMass() - order.getMass() >= 0;
        }

        return false;
    }

    public List<Rocket> getFilteredFlights(float weight, float volume, String urgency) {
        LocalDateTime[] dates = calculateDates(urgency);
        return repo.getFilteredRockets(weight, volume, dates);
    }

    private LocalDateTime[] calculateDates(String urgency) {
        LocalDateTime[] dates = new LocalDateTime[2];
        String[] formattedDates = new String[2];
        dates[0] = LocalDateTime.now().plusYears(35);

        switch (urgency) {
            case "fast":
                dates[1] = dates[0].plusDays(2);
                break;
            case "normal":
                dates[0] = dates[0].plusDays(2);
                dates[1] = dates[0].plusDays(5);
                break;
            case "slow":
                dates[0] = dates[0].plusDays(7);
                dates[1] = dates[0].plusMonths(3);
        }

        formattedDates[0] = dates[0].format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss"));
        formattedDates[1] = dates[1].format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss"));

        return dates;
    }
}
