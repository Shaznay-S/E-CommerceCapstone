package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProductDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;

@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrdersController {

    private ShoppingCartDao shoppingCartDao;
    private ProfileDao profileDao;
    private ProductDao productDao;

    public OrdersController(ShoppingCartDao shoppingCartDao, ProfileDao profileDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.profileDao = profileDao;
        this.productDao = productDao;
    }

//    @PostMapping("")
//    @PreAuthorize("permitAll()")
//    @ResponseStatus(value = HttpStatus.CREATED)
//    public ShoppingCart createOrder(@RequestBody ShoppingCart shoppingCart){
//
//    }
}
