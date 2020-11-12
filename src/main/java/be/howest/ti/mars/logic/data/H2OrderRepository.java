package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class H2OrderRepository implements OrderRepository {

    private static final String SQL_SELECT_ALL_ORDERS = "select * from orders";
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
}