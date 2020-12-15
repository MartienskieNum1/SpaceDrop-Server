package be.howest.ti.mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public class Order {
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    private int orderId;
    private final UUID uuid = UUID.randomUUID();
    private int userId;
    private final int rocketId;
    private final int statusId;

    private final double mass;
    private final double width;
    private final double height;
    private final double depth;
    private final double cost;

    private final Address address;

    @JsonCreator
    public Order(@JsonProperty("orderId") int orderId, @JsonProperty("userId") int userId,
                 @JsonProperty("rocketId") int rocketId, @JsonProperty("statusId") int statusId,
                 @JsonProperty("mass") double mass, @JsonProperty("width") double width,
                 @JsonProperty("height") double height, @JsonProperty("depth") double depth,
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

    public int getOrderId() {
        return orderId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public Address getAddress() {
        return address;
    }

    public double calculateVolume() {
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
