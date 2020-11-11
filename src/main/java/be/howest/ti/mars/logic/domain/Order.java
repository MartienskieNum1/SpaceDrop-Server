package be.howest.ti.mars.logic.domain;

public class Order {
    private int orderId;
    private int userId;
    private int rocketId;
    private int statusId;

    private double mass;
    private double width;
    private double height;
    private double depth;
    private double cost;

    public Order(int userId, int rocketId, int statusId, double mass, double width, double height, double depth, double cost) {
        this.userId = userId;
        this.rocketId = rocketId;
        this.statusId = statusId;
        this.mass = mass;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", rocketId=" + rocketId +
                ", statusId=" + statusId +
                ", mass=" + mass +
                ", width=" + width +
                ", height=" + height +
                ", depth=" + depth +
                ", cost=" + cost +
                '}';
    }
}
