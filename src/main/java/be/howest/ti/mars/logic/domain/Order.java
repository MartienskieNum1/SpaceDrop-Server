package be.howest.ti.mars.logic.domain;

public class Order {
    private int orderId;
    private int userId;
    private int rocketId;
    private int statusId;

    private double mass;
    private double width;
    private double height;
    private double cost;

    public Order(int userId, int rocketId, int statusId, double mass, double width, double height, double cost) {
        this.userId = userId;
        this.rocketId = rocketId;
        this.statusId = statusId;
        this.mass = mass;
        this.width = width;
        this.height = height;
        this.cost = cost;
    }
}
