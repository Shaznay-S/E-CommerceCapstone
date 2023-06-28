package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.ProductDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrdersController {

    private ShoppingCartDao shoppingCartDao;
    private ProfileDao profileDao;
    private OrderDao orderDao;

    @Autowired
    public OrdersController(ShoppingCartDao shoppingCartDao, ProfileDao profileDao, OrderDao orderDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.profileDao = profileDao;
        this.orderDao = orderDao;
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Order createOrder(@RequestBody Order order){
        try{
            orderDao.createOrder(order);
            return order;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

//    @PostMapping("")
//    @PreAuthorize("permitAll()")
//    @ResponseStatus(value = HttpStatus.CREATED)
//    public String createOrder(@RequestParam("user_Id") int userId,
//                                    @RequestParam("address") String address,
//                                    @RequestParam("city") String city,
//                                    @RequestParam("state") String state,
//                                    @RequestParam("zip") String zip){
//
//        Profile profile = profileDao.getById(userId);
//
//        ShoppingCart cart = shoppingCartDao.getByUserId(userId);
//
//        Order order = new Order();
//        order.setProfile(profile);
//        order.setOrderDate(new Date());
//        order.setCart(cart);
//
//        profile.setAddress(address);
//        profile.setCity(city);
//        profile.setState(state);
//        profile.setZip(zip);
//
//        orderDao.createOrder(order);
//
//        shoppingCartDao.clearCart(userId);
//
//        return "redirect:/orderConfirmation";
//
//    }

    @GetMapping("{orderId}")
    public String getOrderDetails(@PathVariable("orderId") int orderId, Model model) {
        Order order = orderDao.getOrderById(orderId);

        if(order != null) {
            model.addAttribute("order", order);
            return "orderDetails";
        }else {
            return "orderNotFound";
        }
    }

    @GetMapping("")
    public String getAllOrders(Model model){
        List<Order> orders = orderDao.getAllOrders();

        model.addAttribute("orders", orders);

        return "orderList";
    }

    @GetMapping("user/{userId}")
    public String getOrdersByUserId(@PathVariable("userId") int userId, Model model){
        List<Order> orders = orderDao.getOrdersByUserId(userId);

        model.addAttribute("orders", orders);

        return "orderList";

    }

    @GetMapping("{orderId}/edit")
    public String showEditOrderForm(@PathVariable("orderId") int orderId, Model model){
        Order order = orderDao.getOrderById(orderId);

        if(order != null) {
            model.addAttribute("order", order);
            return "editOrderForm";
        }else{
            return "orderNotFound";
        }
    }

    @PostMapping("{orderId}/edit")
    public String updateOrder(@PathVariable("orderId") int orderId,
                              @RequestParam("address") String address,
                              @RequestParam("city") String city,
                              @RequestParam("state") String state,
                              @RequestParam("zip") String zip) {

        Order order = orderDao.getOrderById(orderId);

        if(order != null) {
            Profile profile = order.getProfile();
            profile.setAddress(address);
            profile.setCity(city);
            profile.setState(state);
            profile.setZip(zip);
            profileDao.updateProfile(profile.getUserId(), profile);

            orderDao.updateOrder(order);

            return "redirect:/orders/" + orderId;

        }else{
            return "orderNotFound";
        }
    }

    @GetMapping("{orderId}/delete")
    public String showDeleteOrderConfirmation(@PathVariable("orderId") int orderId, Model model){
        Order order = orderDao.getOrderById(orderId);

        if(order != null){
            model.addAttribute("order", order);
            return "deleteOrderConfirmation";
        }else{
            return "orderNotFound";
        }
    }

    @PostMapping("{orderId}/delete")
    public String deleteOrder(@PathVariable("orderId") int orderId){
        orderDao.deleteOrder(orderId);
        return "redirect:/orders";
    }

}
