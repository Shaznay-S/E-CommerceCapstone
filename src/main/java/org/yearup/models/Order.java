package org.yearup.models;

import java.sql.Date;
import java.util.List;

public class Order {

    private int orderId;
    private Profile profile;
    private Date orderDate;
    private ShoppingCart cart;

    public Order(int orderId, Profile profile, Date orderDate, ShoppingCart cart) {
        this.orderId = orderId;
        this.profile = profile;
        this.orderDate = orderDate;
        this.cart = cart;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }
}