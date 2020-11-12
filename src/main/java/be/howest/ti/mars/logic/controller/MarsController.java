package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.MarsRepository;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.MarsException;
import be.howest.ti.mars.logic.util.TokenAES;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class MarsController {
    private final MarsRepository marsRepository = new MarsRepository();

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
}
