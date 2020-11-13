package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Order;
import be.howest.ti.mars.logic.util.MarsException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class H2OrderRepository implements OrderRepository {

    private static final String SQL_SELECT_ALL_ORDERS = "select * from orders";
    private static final String SQL_INSERT_ORDER = "insert into Orders(user_id, rocket_id, status_id, mass, width, height, depth, cost) " +
            "values(?, ?, ?, ?, ?, ?, ?, ?)";

    private static final Logger LOGGER = Logger.getLogger(H2OrderRepository.class.getName());

    @Override
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();

        try (Connection con = MarsRepository.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL_SELECT_ALL_ORDERS);
             ResultSet results = stmt.executeQuery()) {
            while (results.next()) {
                int orderId = results.getInt("id");
                int userId = results.getInt("user_id");
                int rocketId = results.getInt("rocket_id");
                int statusId = results.getInt("status_id");
                double mass = results.getDouble("mass");
                double width = results.getDouble("width");
                double height = results.getDouble("height");
                double depth = results.getDouble("depth");
                double cost = results.getDouble("cost");

                orders.add(new Order(orderId, userId, rocketId, statusId, mass, width, height, depth, cost));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new IllegalStateException("Failed to get all orders");
        }

        return orders;
    }

    @Override
    public Order createOrder(Order order) {
        try (Connection con = MarsRepository.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getUserId());
            stmt.setInt(2, order.getRocketId());
            stmt.setInt(3, order.getStatusId());
            stmt.setDouble(4, order.getMass());
            stmt.setDouble(5, order.getWidth());
            stmt.setDouble(6, order.getHeight());
            stmt.setDouble(7, order.getDepth());
            stmt.setDouble(8, order.getCost());

            stmt.executeUpdate();

            try (ResultSet rsKey = stmt.getGeneratedKeys()) {
                rsKey.next();

                order.setOrderId(rsKey.getInt(1));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new MarsException("Creating new order failed");
        }

        return order;
    }
}
