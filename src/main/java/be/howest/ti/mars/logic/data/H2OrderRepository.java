package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class H2OrderRepository implements OrderRepository {

    private static final String SQL_SELECT_ALL_ORDERS = "select * from orders";

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

                orders.add(new Order(userId, rocketId, statusId, mass, width, height, depth, cost));
            }
        } catch (SQLException e) {
            throw new IllegalStateException();
        }

        return orders;
    }
}
