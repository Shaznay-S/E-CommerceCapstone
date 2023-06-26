package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao {

    ShoppingCart getByUserId(int userId);
    // add additional method signatures here

    void saveCart(ShoppingCart cart);

    void clearCart(ShoppingCart cart);

}
