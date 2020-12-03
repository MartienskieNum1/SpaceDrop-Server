package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.MarsException;
import be.howest.ti.mars.logic.util.TokenAES;
import be.howest.ti.mars.logic.data.MarsRepository;
import be.howest.ti.mars.logic.data.OrderRepository;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Order;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MarsController {
    private final MarsRepository marsRepository = new MarsRepository();
    OrderRepository orderRepo = Repositories.getOrderRepo();
    private static final Logger LOGGER = Logger.getLogger(MarsController.class.getName());

    public String getMessage() {
        return "Hello, Mars!";
    }

    public String createUser(User user) {
        try {
            marsRepository.createUser(user);
            return TokenAES.encrypt(user.getEmail());
        } catch (MarsException ex) {
            return null;
        }
    }

    public String login(String email, String password) {
        try {
            User user = marsRepository.getUserViaEmail(email);
            if (BCrypt.checkpw(password, user.getPassword()))
                return TokenAES.encrypt(email);
            return null;
        } catch (MarsException ex) {
            return null;
        }
    }

    public String setUser(String email, String oldPassword, User moddedUser) {
        User ogUser = marsRepository.getUserViaEmail(email);
        if (BCrypt.checkpw(oldPassword, ogUser.getPassword())) {
            marsRepository.setUser(ogUser, moddedUser);
            return TokenAES.encrypt(moddedUser.getEmail());
        } else {
            return null;
        }
    }

    public boolean userExists(String email) {
        try {
            marsRepository.getUserViaEmail(email);
            return true;
        } catch (MarsException ex) {
            return false;
        }
    }

    public Role getRoleViaEmail(String email) {
        return marsRepository.getRoleViaEmail(email);
    }

    public List<User> getUsers() {
        return new ArrayList<>(marsRepository.getUsers());
    }

    public User getUser(String email) {
        return marsRepository.getUserViaEmail(email);
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orderRepo.getOrders());
    }

    public Order createOrder(Order newOrder) {
        return orderRepo.createOrder(newOrder);
    }

    public Order getOrderById(int orderId) {
        return orderRepo.getOrderById(orderId);
    }

    public Object getRockets() {
        return marsRepository.getRockets();
    }

    public int getUserId(String email) {
        return marsRepository.getIdViaEmail(email);
    }

    public List<Order> getOrdersForUser(String email) {
        return orderRepo.getOrdersForUser(email);
    }

    public Map<Integer, String> getIdsForStatuses() {
        return orderRepo.getIdsForStatuses();
    }
}
