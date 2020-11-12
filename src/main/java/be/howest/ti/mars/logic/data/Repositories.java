package be.howest.ti.mars.logic.data;

public class Repositories {
    private static final OrderRepository ORDER_REPO = new H2OrderRepository();

    private Repositories() {}

    public static OrderRepository getOrderRepo() {
        return ORDER_REPO;
    }
}
