package org.yearup.data;

import org.yearup.models.Order;

import java.util.List;

public interface OrderDao {

    Order createOrder(Order order);
    Order getOrderById(int orderId);
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(int userId);
    void updateOrder(Order order);
    void deleteOrder(int orderId);

}
