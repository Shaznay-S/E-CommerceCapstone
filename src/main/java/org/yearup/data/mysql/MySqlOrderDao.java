package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Order createOrder(Order order){
        String sql = "INSERT INTO orders(user_id, date, address, city, state, zip, shipping_ampunt)"+
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    private void createOrderLineItem(int orderId, ShoppingCartItem item){
        String sql = "INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount)" +
                "VALUES (?, ?, ?, ?, ?)";

        try(Connection connection = getConnection()){
            PreparedStatement ps = connection.prepareStatement( sql);

            ps.setInt(1, orderId);
            ps.setInt(2, item.getProductId());
            ps.setBigDecimal(3, item.getLineTotal());
            ps.setInt(4, item.getQuantity());
            ps.setBigDecimal(5, item.getDiscountPercent());

            ps.executeUpdate();

        }catch(SQLException e){

            throw new RuntimeException(e);

        }
    }
}
