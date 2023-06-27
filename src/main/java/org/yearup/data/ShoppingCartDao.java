package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao {

    ShoppingCart getByUserId(int userId);
    // add additional method signatures here

    void saveCart(int userId, int productId, int quantity);

    void clearCart(int userId);

    void addToCart(int userId, int productId, ShoppingCartItem item);

}