package org.yearup.data.mysql;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Order createOrder(Order order){
        String sql = "INSERT INTO orders(user_id, date, address, city, state, zip, shipping_amount)"+
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){


            ps.setInt(1, order.getProfile().getUserId());
            ps.setDate(2, order.getOrderDate());
            ps.setString(3, order.getProfile().getAddress());
            ps.setString(4, order.getProfile().getCity());
            ps.setString(5, order.getProfile().getState());
            ps.setString(6, order.getProfile().getZip());
            ps.setBigDecimal(7, order.getCart().getTotal());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);
                        order.setOrderId(orderId);
                    }
                }
            }

            return order;

        }catch(SQLException e){

            throw new RuntimeException(e);

        }
    }

    @Override
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Order order = mapOrder(rs);

                    return order;

                }else{
                    return null;
                }
            }
        } catch (SQLException e) {

            throw new RuntimeException(e);

        }

    }

    @Override
    public List<Order> getAllOrders(){
        String sql = "SELECT * FROM orders";
        try(Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){

            List<Order> orders = new ArrayList<>();
            while (rs.next()){
                Order order = mapOrder(rs);
                orders.add(order);
            }

            return orders;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(int userId){
        String sql = "SELECT * FROM orders WHERE user_id = ?";

        try(Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, userId);

            try(ResultSet rs = ps.executeQuery()){
                List<Order> orders = new ArrayList<>();
                while (rs.next()){
                    Order order = mapOrder(rs);
                    orders.add(order);
                }
                return orders;
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateOrder(Order order){
        String sql = "UPDATE orders SET user_id = ?, " +
                "date = ?, address = ?, city = ?, state = ?, zip = ?, shipping_amount = ?" +
                "WHERE order_id = ?";

        try(Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, order.getProfile().getUserId());
            ps.setDate(2, order.getOrderDate());
            ps.setString(3, order.getProfile().getAddress());
            ps.setString(4, order.getProfile().getCity());
            ps.setString(5, order.getProfile().getState());
            ps.setString(6, order.getProfile().getZip());
            ps.setBigDecimal(7, order.getCart().getTotal());
            ps.setInt(8, order.getOrderId());

            ps.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void deleteOrder(int orderId){
        String sql = "DELETE FROM orders WHERE order_id = ?";

        try(Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, orderId);
            ps.executeUpdate();

        }catch (SQLException e){

            throw new RuntimeException(e);

        }
    }

    private Order mapOrder(ResultSet row) throws SQLException{
        int orderId = row.getInt("order_id");

        int userId = row.getInt("user_id");
        ProfileDao profileDao = null;
        Profile profile = profileDao.getById(userId);

        Date orderDate = row.getDate("date");

        int userId2 = row.getInt("user_id");
        ShoppingCartDao cartDao = null;
        ShoppingCart cart = cartDao.getByUserId(userId2);
//        BigDecimal shippingAmount = cart.getTotal();

        return new Order(orderId, profile, orderDate, cart);
    }
}
