package org.yearup.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ItemDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

@RestController
@RequestMapping("orderItems")
@CrossOrigin
public class ItemsController {

    private ItemDao itemDao;
    private ShoppingCartDao cartDao;

    @Autowired
    public ItemsController(ItemDao itemDao, ShoppingCartDao cartDao) {
        this.itemDao = itemDao;
        this.cartDao = cartDao;
    }

//    @PostMapping("cart/items")
//    public String addItemToCart(@RequestParam("userId") int userId,
//                                @RequestParam("productId") int productId,
//                                @RequestParam("quantity") int quantity){
//        ShoppingCart cart = cartDao.getByUserId(userId);
//        cart.add(productId, quantity);
//
//        return "redirect:/shoppingCart";
//    }
//
//    @PostMapping("cart/items/{productId}/quantity")
//    public String updateItemQuantity(@PathVariable("productId") int productId,
//                                     @RequestParam("quantity") int quantity){
//        ShoppingCartItem item = itemDao.getItemById(productId);
//
//        if(item != null) {
//            item.setQuantity(quantity);
//            itemDao.updateItem(item);
//        }
//
//        return "redirect:/shoppingCart";
//    }
//
//    @GetMapping("/cart/items/{productId}/delete")
//    public String deleteItem(@PathVariable("productId") int productId){
//        itemDao.deleteItem(productId);
//        return "redirect:/shoppingCart";
//    }

}
