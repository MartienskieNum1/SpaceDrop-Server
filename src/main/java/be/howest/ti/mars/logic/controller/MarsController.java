package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.MarsRepository;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.TokenAES;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class MarsController {

    public String getMessage() {
        return "Hello, Mars!";
    }

    public String createUser(User user) {
        MarsRepository.createUser(user);
        return TokenAES.encrypt(user.getEmail());
    }

    public JsonArray getUsers() {
        JsonArray array = new JsonArray();
        for (User user : MarsRepository.getUsers()) {
            array.add(JsonObject.mapFrom(user));
        }
        return array;
    }
}
