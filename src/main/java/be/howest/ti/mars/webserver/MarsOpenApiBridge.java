package be.howest.ti.mars.webserver;

import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.data.H2OrderRepository;
import io.vertx.ext.web.RoutingContext;

import java.util.logging.Logger;

class MarsOpenApiBridge {
    private final MarsController controller;

    MarsOpenApiBridge() {
        this.controller = new MarsController();
    }

    public Object getMessage(RoutingContext ctx) {
        return controller.getMessage();
    }

    public Object getOrders(RoutingContext ctx) {
        return controller.getOrders();
    }
}
