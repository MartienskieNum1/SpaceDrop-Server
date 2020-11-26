package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Role;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.MarsException;
import be.howest.ti.mars.logic.util.TokenAES;
import be.howest.ti.mars.logic.data.MarsRepository;
import be.howest.ti.mars.logic.data.OrderRepository;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Order;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MarsController {
    private final MarsRepository marsRepository = new MarsRepository();
    OrderRepository orderRepo = Repositories.getOrderRepo();
    private static final Logger LOGGER = Logger.getLogger(MarsController.class.getName());

    public String getMessage() {
        return "Hello, Mars!";
    }

    public String createUser(User user) {
        marsRepository.createUser(user);
        return TokenAES.encrypt(user.getEmail());
    }

    public String login(String email, String password) {
        try {
            marsRepository.getUserViaLogin(email, TokenAES.encrypt(password));
            return TokenAES.encrypt(email);
        } catch (MarsException ex) {
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

    public JsonArray getUsers() {
        JsonArray array = new JsonArray();
        marsRepository.getUsers().forEach(user -> array.add(JsonObject.mapFrom(user)));
        return array;
    }

    public List<JsonObject> getOrders() {
        List<JsonObject> jsonList = new ArrayList<>();

        for (Order order : orderRepo.getOrders()) {
            JsonObject json = new JsonObject();
            json.put("orderId", order.getOrderId());
            json.put("userId", order.getUserId());
            json.put("rocketId", order.getRocketId());
            json.put("statusId", order.getStatusId());
            json.put("mass", order.getMass());
            json.put("width", order.getWidth());
            json.put("height", order.getHeight());
            json.put("depth", order.getDepth());
            json.put("cost", order.getCost());

            jsonList.add(json);
        }

        return jsonList;
    }

    public Order createOrder(Order newOrder) {
        return orderRepo.createOrder(newOrder);
    }

    public Object getOrderById(int orderId) {
        Order order = orderRepo.getOrderById(orderId);

        return JsonObject.mapFrom(order);
    }
}
