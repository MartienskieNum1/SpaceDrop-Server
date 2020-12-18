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
    private UUID uuid;
    private int userId;
    private final int rocketId;
    private final int statusId;
    private final String status;

    private final float mass;
    private final float width;
    private final float height;
    private final float depth;
    private final double cost;

    private final Address address;

    @JsonCreator
    public Order(@JsonProperty("orderId") int orderId, @JsonProperty("uuid") UUID uuid, @JsonProperty("userId") int userId,
                 @JsonProperty("rocketId") int rocketId, @JsonProperty("statusId") int statusId, @JsonProperty("status") String status,
                 @JsonProperty("mass") float mass, @JsonProperty("width") float width,
                 @JsonProperty("height") float height, @JsonProperty("depth") float depth,
                 @JsonProperty("cost") double cost, @JsonProperty("address") Address address) {
        this.orderId = orderId;
        this.uuid = uuid;
        this.userId = userId;
        this.rocketId = rocketId;
        this.statusId = statusId;
        this.status = status;
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

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public String getStatus() {
        return status;
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
