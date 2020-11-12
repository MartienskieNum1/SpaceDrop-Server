package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.OrderRepository;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Order;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class MarsController {
    OrderRepository repo = Repositories.getOrderRepo();

    public String getMessage() {
        return "Hello, Mars!";
    }

    public JsonArray getUsers() {
        JsonArray jsonArray = new JsonArray();

        for (Order order : repo.getOrders()) {
            jsonArray.add(JsonObject.mapFrom(order));
        }

        return jsonArray;
    }
}
