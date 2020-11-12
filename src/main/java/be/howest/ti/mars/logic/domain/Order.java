package be.howest.ti.mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonCreator
    public Order(@JsonProperty("orderId") int orderId, @JsonProperty("userId") int userId,
                 @JsonProperty("rocketId") int rocketId, @JsonProperty("statusId") int statusId,
                 @JsonProperty("mass") double mass, @JsonProperty("width") double width,
                 @JsonProperty("height") double height, @JsonProperty("depth") double depth,
                 @JsonProperty("cost") double cost) {
        this.orderId = orderId;
        this.userId = userId;
        this.rocketId = rocketId;
        this.statusId = statusId;
        this.mass = mass;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.cost = cost;
    }

    public Order(@JsonProperty("userId") int userId,
                 @JsonProperty("rocketId") int rocketId, @JsonProperty("statusId") int statusId,
                 @JsonProperty("mass") double mass, @JsonProperty("width") double width,
                 @JsonProperty("height") double height, @JsonProperty("depth") double depth,
                 @JsonProperty("cost") double cost) {
        this(-1, userId, rocketId, statusId, mass, width, height, depth, cost);
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public int getRocketId() {
        return rocketId;
    }

    public int getStatusId() {
        return statusId;
    }

    public double getMass() {
        return mass;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDepth() {
        return depth;
    }

    public double getCost() {
        return cost;
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
