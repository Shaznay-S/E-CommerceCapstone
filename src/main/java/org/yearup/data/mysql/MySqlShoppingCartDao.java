package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId){
        String sql = "SELECT * FROM easyshop.shopping_cart\n" +
                "JOIN products on shopping_cart.product_id = products.product_id\n" +
                "WHERE user_id = ?;";
        ShoppingCart shoppingCart = new ShoppingCart();
        try (Connection connection = getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    ShoppingCartItem item = new ShoppingCartItem();
                    Product products = new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getBigDecimal("price"),
                            rs.getInt("category_id"),
                            rs.getString("description"),
                            rs.getString("color"),
                            rs.getInt("stock"),
                            rs.getBoolean("featured"),
                            rs.getString("image_url"));
//                    item.setQuantity(rs.getInt("quantity");
                    item.setProduct(products);
                    shoppingCart.add(item);
                }
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return shoppingCart;
    }

    @Override
    public void addToCart(int userId, int productId, ShoppingCartItem item){
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity)" +
                "VALUES(?, ?, ?)";
        try(Connection connection = getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, item.getQuantity());
            ps.executeUpdate();
//            return item;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveCart(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart " +
                "SET quantity = ? " +
                "WHERE user_id = ? AND product_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart " +
                "WHERE user_id = ?";
        try(Connection connection = getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);

        }
    }

}