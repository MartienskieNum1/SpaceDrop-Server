package be.howest.ti.mars.webserver;

import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.domain.Order;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.util.TokenAES;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class MarsOpenApiBridge {
    private final MarsController controller;

    MarsOpenApiBridge() {
        this.controller = new MarsController();
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

    public Object getOrders(RoutingContext ctx) {
        return controller.getOrders();
    }

    public Order createOrder(RoutingContext ctx) {
        String body = ctx.getBodyAsString();
        Order newOrder = Json.decodeValue(body, Order.class);
        return controller.createOrder(newOrder);
    }

    public Object createUser(RoutingContext ctx) {
        String body = ctx.getBodyAsString();
        User newUser = Json.decodeValue(body, User.class);
        return controller.createUser(newUser);
    }

    public Object getUsers(RoutingContext ctx) {
        return controller.getUsers();
    }

    public Object getUser(RoutingContext ctx) {
        String token = ctx.request().getHeader(HttpHeaders.AUTHORIZATION);
        String email = TokenAES.decrypt(token);
        return controller.getUser(email);
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

    public Object getOrderById(RoutingContext ctx) {
        int orderId = Integer.parseInt(ctx.request().getParam("id"));
        return controller.getOrderById(orderId);
    }

    public Object getRockets(RoutingContext ctx) {
        return controller.getRockets();
    }

    public Object getOrdersForUser(RoutingContext ctx) {
        String token = ctx.request().getHeader(HttpHeaders.AUTHORIZATION);
        String email = TokenAES.decrypt(token);
        Map<Integer, String> statuses = controller.getIdsForStatuses();
        List<Order> orders = controller.getOrdersForUser(email);
        List<JsonObject> jsonList = new ArrayList<>();

        for (Order order: orders) {
            JsonObject json = new JsonObject();
            json.put("orderId", order.getOrderId());
            json.put("userId", order.getUserId());
            json.put("rocketId", order.getRocketId());
            json.put("status", statuses.get(order.getStatusId()));
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
