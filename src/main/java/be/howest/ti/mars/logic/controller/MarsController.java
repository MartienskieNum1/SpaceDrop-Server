package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.MarsRepository;
import be.howest.ti.mars.logic.domain.User;
import io.vertx.core.json.JsonObject;

public class MarsController {

    public String getMessage() {
        return "Hello, Mars!";
    }

    public JsonObject createUser(User user) {
        MarsRepository.createUser(user);
        return JsonObject.mapFrom(user);
    }
}
