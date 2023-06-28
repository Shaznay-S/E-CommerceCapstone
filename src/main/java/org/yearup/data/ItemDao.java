package org.yearup.data;

import org.yearup.models.ShoppingCartItem;

public interface ItemDao {

    void createItem(int orderId, ShoppingCartItem item);
    void updateItem(int orderId, ShoppingCartItem item);
    void deleteItem(int orderId, int productId);

}
