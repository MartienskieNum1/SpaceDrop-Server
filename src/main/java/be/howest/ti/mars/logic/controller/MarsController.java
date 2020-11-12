package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.H2OrderRepository;
import be.howest.ti.mars.logic.data.MarsRepository;
import be.howest.ti.mars.logic.data.OrderRepository;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Order;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import be.howest.ti.mars.logic.data.MarsRepository;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.MarsException;
import be.howest.ti.mars.logic.util.TokenAES;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class MarsController {
    private final MarsRepository marsRepository = new MarsRepository();
    OrderRepository repo = Repositories.getOrderRepo();
    private static final Logger LOGGER = Logger.getLogger(MarsController.class.getName());

    public String getMessage() {
        return "Hello, Mars!";
    }

    public String createUser(User user) {
        marsRepository.createUser(user);
        return TokenAES.encrypt(user.getEmail());
    }

    public boolean userExists(String token) {
        String email = TokenAES.decrypt(token);
        try {
            marsRepository.getUserOnEmail(email);
            return true;
        } catch (MarsException ex) {
            return false;
        }
    }

    public JsonArray getUsers() {
        JsonArray array = new JsonArray();
        marsRepository.getUsers().forEach(user -> array.add(JsonObject.mapFrom(user)));
        return array;
    }

    public List<JsonObject> getOrders() {
        List<JsonObject> jsonList = new ArrayList<>();

        for (Order order : repo.getOrders()) {
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
}
