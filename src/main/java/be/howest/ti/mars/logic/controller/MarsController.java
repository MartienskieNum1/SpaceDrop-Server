package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.MarsException;
import be.howest.ti.mars.logic.util.TokenAES;
import be.howest.ti.mars.logic.data.H2Repository;
import be.howest.ti.mars.logic.domain.Order;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MarsController {
    private final H2Repository h2Repository = new H2Repository();
    private static final Logger LOGGER = Logger.getLogger(MarsController.class.getName());

    public String getMessage() {
        return "Hello, Mars!";
    }

    public String createUser(User user) {
        try {
            h2Repository.createUser(user);
            return TokenAES.encrypt(user.getEmail());
        } catch (MarsException ex) {
            return null;
        }
    }

    public String login(String email, String password) {
        try {
            User user = h2Repository.getUserViaEmail(email);
            if (BCrypt.checkpw(password, user.getPassword()))
                return TokenAES.encrypt(email);
            return null;
        } catch (MarsException ex) {
            return null;
        }
    }

    public String setUser(String email, String oldPassword, User moddedUser) {
        User ogUser = h2Repository.getUserViaEmail(email);
        if (BCrypt.checkpw(oldPassword, ogUser.getPassword())) {
            h2Repository.setUser(ogUser, moddedUser);
            return TokenAES.encrypt(moddedUser.getEmail());
        } else {
            return null;
        }
    }

    public boolean userExists(String email) {
        try {
            h2Repository.getUserViaEmail(email);
            return true;
        } catch (MarsException ex) {
            return false;
        }
    }

    public Role getRoleViaEmail(String email) {
        return h2Repository.getRoleViaEmail(email);
    }

    public List<User> getUsers() {
        return new ArrayList<>(h2Repository.getUsers());
    }

    public User getUser(String email) {
        return h2Repository.getUserViaEmail(email);
    }

    public List<Order> getOrders() {
        return new ArrayList<>(h2Repository.getOrders());
    }

    public Order createOrder(Order newOrder) {
        return h2Repository.createOrder(newOrder);
    }

    public Order getOrderById(int orderId) {
        return h2Repository.getOrderById(orderId);
    }

    public Object getRockets() {
        return h2Repository.getRockets();
    }

    public int getUserId(String email) {
        return h2Repository.getIdViaEmail(email);
    }

    public List<Order> getOrdersForUser(String email) {
        return h2Repository.getOrdersForUser(email);
    }

    public Map<Integer, String> getIdsForStatuses() {
        return h2Repository.getIdsForStatuses();
    }
}
