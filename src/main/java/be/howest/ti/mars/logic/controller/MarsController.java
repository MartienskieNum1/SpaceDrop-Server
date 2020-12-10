package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.MarsRepository;
import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.MarsException;
import be.howest.ti.mars.logic.util.TokenAES;
import be.howest.ti.mars.logic.domain.Order;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MarsController {
    private final MarsRepository repo;
    private static final Logger LOGGER = Logger.getLogger(MarsController.class.getName());

    public MarsController(MarsRepository repo) {
        this.repo = repo;
    }

    public String getMessage() {
        return "Hello, Mars!";
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

    public Order createOrder(Order newOrder) {
        return repo.createOrder(newOrder);
    }

    public Order getOrderById(int orderId) {
        try {
            return repo.getOrderById(orderId);
        } catch (IllegalStateException ex) {
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
        return repo.getOrdersForUser(email);
    }

    public Map<Integer, String> getIdsForStatuses() {
        return repo.getIdsForStatuses();
    }
}
