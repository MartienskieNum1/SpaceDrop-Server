package be.howest.ti.mars.webserver;

import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.domain.User;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

class MarsOpenApiBridge {
    private final MarsController controller;

    MarsOpenApiBridge() {
        this.controller = new MarsController();
    }

    public boolean verifyUserToken(String token) {
        return controller.userExists(token);
    }

    public Object getMessage(RoutingContext ctx) {
        return controller.getMessage();
    }

    public Object createUser(RoutingContext ctx) {
        String body = ctx.getBodyAsString();
        User newUser = Json.decodeValue(body, User.class);
        return controller.createUser(newUser);
    }

    public Object getUsers(RoutingContext ctx) {
        return controller.getUsers();
    }
}
