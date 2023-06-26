package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import java.security.Principal;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            return shoppingCartDao.getByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping("/products/{productId}")
    public void addToCart(Principal principal, @PathVariable int productId) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            if (cart == null) {
                cart = new ShoppingCart();
            }
            ShoppingCartItem item = cart.get(productId);
            if (item == null) {
                item = new ShoppingCartItem();
                item.setQuantity(1);
                cart.add(item);
            } else {
                item.setQuantity(item.getQuantity() + 1);
            }
            shoppingCartDao.saveCart(cart);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("/products/{productId}")
    public void updateCartItem(Principal principal, @PathVariable int productId, @RequestBody ShoppingCartItem item) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            if (cart == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping cart not found.");
            }
            ShoppingCartItem existingItem = cart.get(productId);
            if (existingItem == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found in the cart.");
            }
            existingItem.setQuantity(item.getQuantity());
            shoppingCartDao.saveCart(cart);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping
    public void clearCart(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            if (cart == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping cart not found.");
            }
            shoppingCartDao.clearCart(cart);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}