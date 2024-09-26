package com.milan.codechangepresentationgenerator.cart.controller;

import com.milan.codechangepresentationgenerator.cart.dto.CartDto;
import com.milan.codechangepresentationgenerator.cart.dto.CartResponseDto;
import com.milan.codechangepresentationgenerator.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping("/user/{userId}")
    public List<CartResponseDto> getCartForUser(@PathVariable int userId) {
        return cartService.getCartForUser(userId);
    }

    @GetMapping("/is-product-in-cart")
    public Boolean isProductInCart(@RequestParam int userId, @RequestParam int productId) {
        return cartService.isProductInCart(userId, productId);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestBody CartDto cartDto) {
        cartService.addItemToCart(cartDto);
        return new ResponseEntity<>("Item added to the cart successfully.",HttpStatus.OK);
    }

    @PutMapping("/update")
    public String updateCartItem(@RequestBody CartDto cartDto) {
        cartService.updateCartItem(cartDto);
        return ("Cart item updated successfully.");
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<?> removeCartItem(@PathVariable int id) {
        cartService.removeCartItem(id);
        return ResponseEntity.ok().body("{\"message\": \"Cart item removed successfully.\"}");

    }

    @PutMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable int userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().body("{\"message\": \"Cart cleared successfully.\"}");
    }

    @GetMapping("/count/{userId}")
    public Integer getActiveCartCountByUserId(@PathVariable int userId) {
        return cartService.getActiveCartCountByUserId(userId);
    }
}
