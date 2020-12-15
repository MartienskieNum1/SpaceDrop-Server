package be.howest.ti.mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Order {
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    private int orderId;
    private int userId;
    private int rocketId;
    private int statusId;

    private float mass;
    private float width;
    private float height;
    private float depth;
    private double cost;

    private Address address;

    @JsonCreator
    public Order(@JsonProperty("orderId") int orderId, @JsonProperty("userId") int userId,
                 @JsonProperty("rocketId") int rocketId, @JsonProperty("statusId") int statusId,
                 @JsonProperty("mass") float mass, @JsonProperty("width") float width,
                 @JsonProperty("height") float height, @JsonProperty("depth") float depth,
                 @JsonProperty("cost") double cost, @JsonProperty("address") Address address) {
        this.orderId = orderId;
        this.userId = userId;
        this.rocketId = rocketId;
        this.statusId = statusId;
        this.mass = mass;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.cost = cost;
        this.address = address;
    }

    /*@JsonCreator
    public Order(@JsonProperty("userId") int userId,
                 @JsonProperty("rocketId") int rocketId, @JsonProperty("statusId") int statusId,
                 @JsonProperty("mass") double mass, @JsonProperty("width") double width,
                 @JsonProperty("height") double height, @JsonProperty("depth") double depth,
                 @JsonProperty("cost") double cost) {
        this(-1, userId, rocketId, statusId, mass, width, height, depth, cost);
    }*/

    public void setUserId(int userId) {
        this.userId = userId;
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

    public float getMass() {
        return mass;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getDepth() {
        return depth;
    }

    public double getCost() {
        return cost;
    }

    public Address getAddress() {
        return address;
    }

    public float calculateVolume() {
        return width * height * depth;
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
                ", address=" + address +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
