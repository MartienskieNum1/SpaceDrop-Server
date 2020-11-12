package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.OrderRepository;
import be.howest.ti.mars.logic.data.Repositories;
import io.vertx.core.json.JsonArray;

public class MarsController {
    OrderRepository repo = Repositories.getOrderRepo();

    public String getMessage() {
        return "Hello, Mars!";
    }

    public JsonArray getUsers() {
        return null;
    }
}
