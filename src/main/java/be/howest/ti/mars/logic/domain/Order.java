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
    public Order(@JsonProperty("userId") int userId, @JsonProperty("rocketId") int rocketId,
                 @JsonProperty("statusId") int statusId, @JsonProperty("mass") double mass,
                 @JsonProperty("width") double width, @JsonProperty("height") double height,
                 @JsonProperty("depth") double depth, @JsonProperty("cost") double cost) {
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
