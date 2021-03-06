package be.howest.ti.mars.webserver;

import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Order;
import be.howest.ti.mars.logic.domain.Rocket;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.TokenAES;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.validation.ValidationException;

import java.util.List;
import java.util.UUID;

class MarsOpenApiBridge {
    private final MarsController controller;

    MarsOpenApiBridge() {
        this.controller = new MarsController(Repositories.H2REPO);
    }

    public boolean verifyUserToken(String token) {
        String email = TokenAES.decrypt(token);
        return controller.userExists(email);
    }

    public boolean verifyAdminToken(String token) {
        String email = TokenAES.decrypt(token);
        return controller.getRoleViaEmail(email).getName().equals("Admin");
    }

    public Object getMessage(RoutingContext ctx) {
        return controller.getMessage();
    }

    public Object createUser(RoutingContext ctx) {
        String body = ctx.getBodyAsString();
        User newUser = Json.decodeValue(body, User.class);
        String token = controller.createUser(newUser);
        if (token == null)
            ctx.fail(409);
        return token;
    }

    public Object getUsers(RoutingContext ctx) {
        return controller.getUsers();
    }

    public Object getUser(RoutingContext ctx) {
        String token = ctx.request().getHeader(HttpHeaders.AUTHORIZATION);
        String email = TokenAES.decrypt(token);
        User user = controller.getUser(email);
        if (user == null)
            ctx.fail(500);
        return user;
    }

    public Object login(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        String email = body.getString("email");
        String password = body.getString("password");
        String token = controller.login(email, password);
        if (token == null)
            ctx.fail(403);
        return token;
    }

    public Object setUser(RoutingContext ctx) {
        String token = ctx.request().getHeader(HttpHeaders.AUTHORIZATION);
        String email = TokenAES.decrypt(token);

        JsonArray body = ctx.getBodyAsJsonArray();
        String password = body.getString(0);
        JsonObject userObject = body.getJsonObject(1);
        String userString = userObject.encode();
        User user = Json.decodeValue(userString, User.class);

        String newToken = controller.setUser(email, password, user);
        if (newToken == null)
            ctx.fail(403);
        return newToken;
    }

    public Object getOrders(RoutingContext ctx) {
        return controller.getOrders();
    }

    public Order createOrder(RoutingContext ctx) {
        Order newOrder = Json.decodeValue(ctx.getBodyAsString(), Order.class);
        Order createdOrder;

        if (ctx.request().getHeader("Authorization").equals("")) {
            createdOrder = controller.createOrder(newOrder, -10);
        } else {
            int userId = getUserId(ctx);
            createdOrder = controller.createOrder(newOrder, userId);
        }

        if (createdOrder == null) {
            ctx.fail(400, new ValidationException("Package too big or too heavy or rocket departed"));
        }

        return createdOrder;
    }

    public Object getOrderById(RoutingContext ctx) {
        int orderId = Integer.parseInt(ctx.request().getParam("id"));
        Order order = controller.getOrderById(orderId);
        if (order == null)
            ctx.fail(404);
        return order;
    }

    public Object getOrderByUuid(RoutingContext ctx) {
        String uuid = ctx.request().getParam("uuid");
        Order order = controller.getOrderByUuid(UUID.fromString(uuid));
        if (order == null)
            ctx.fail(404);
        return order;
    }

    public Object getRockets(RoutingContext ctx) {
        return controller.getRockets();
    }

    public Object getOrdersForUser(RoutingContext ctx) {
        List<Order> orders = controller.getOrdersForUser(decryptTokenToEmail(ctx));

        if (orders == null) {
            ctx.fail(500);
        }
        return orders;
    }

    public int getUserId(RoutingContext ctx) {
        int id = controller.getUserId(decryptTokenToEmail(ctx));

        if (id == -1)
            ctx.fail(500);
        return id;
    }

    private String decryptTokenToEmail(RoutingContext ctx) {
        return TokenAES.decrypt(ctx.request().getHeader(HttpHeaders.AUTHORIZATION));
    }

    public List<Rocket> getFilteredFlights(RoutingContext ctx) {
        float weight = Float.parseFloat(ctx.request().getParam("weight"));
        float volume = Float.parseFloat(ctx.request().getParam("volume"));
        String urgency = ctx.request().getParam("urgency");

        return controller.getFilteredFlights(weight, volume, urgency);
    }
}
