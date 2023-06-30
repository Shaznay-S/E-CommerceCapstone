package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ItemDao;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlItemDao extends MySqlDaoBase implements ItemDao {

    public MySqlItemDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void createItem(int orderId, ShoppingCartItem item){
        String sql = "INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount)" +
                "VALUES (?, ?, ?, ?, ?)";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, orderId);
            ps.setInt(2, item.getProductId());
            ps.setBigDecimal(3, item.getProduct().getPrice());
            ps.setInt(4, item.getQuantity());
            ps.setBigDecimal(5, item.getDiscountPercent());

            ps.executeUpdate();

        }catch(SQLException e){

            throw new RuntimeException(e);

        }
    }

    @Override
    public void updateItem(int orderId, ShoppingCartItem item){
        String sql = "UPDATE order_line_items SET sales_price = ?, quantity = ?, discount = ? " +
                "WHERE order_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setBigDecimal(1, item.getProduct().getPrice());
            ps.setInt(2, item.getQuantity());
            ps.setBigDecimal(3, item.getDiscountPercent());
            ps.setInt(4, orderId);
            ps.setInt(5, item.getProductId());

            ps.executeUpdate();

        }catch (SQLException e){

            throw new RuntimeException(e);

        }
    }

//    @Override
//    public ShoppingCartItem getById(int productId){
//        String sql = "SELECT * FROM order_line_items WHERE order_line_item_id = ?";
//
//        try (Connection connection = getConnection();
//             PreparedStatement ps = connection.prepareStatement(sql)) {
//
//            ps.setInt(1, productId);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                ShoppingCartItem item = new ShoppingCartItem();
//                item.setUserId(rs.getInt("user_id"));
//                item.setFirstName(rs.getString("first_name"));
//                profile.setLastName(rs.getString("last_name"));
//                profile.setPhone(rs.getString("phone"));
//                profile.setEmail(rs.getString("email"));
//                profile.setAddress(rs.getString("address"));
//                profile.setCity(rs.getString("city"));
//                profile.setState(rs.getString("state"));
//                profile.setZip(rs.getString("zip"));
//
//                return item;
//            }
//        } catch (SQLException sqlException) {
//            sqlException.printStackTrace();
//        }
//
//        return null;
//    }

    @Override
    public void deleteItem(int orderId, int productId){
        String sql = "DELETE FROM order_line_items WHERE order_id = ? AND product_id = ?";

        try(Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, orderId);
            ps.setInt(2, productId);

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
