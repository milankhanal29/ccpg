package com.milan.codechangepresentationgenerator.cart.service;

import com.milan.codechangepresentationgenerator.cart.dto.CartDto;
import com.milan.codechangepresentationgenerator.cart.dto.CartResponseDto;
import com.milan.codechangepresentationgenerator.shared.status.Status;

import java.util.List;

public interface CartService {

    void addItemToCart(CartDto cartDto);

    void updateCartItem(CartDto cartDto);

    void removeCartItem(int id);

    void clearCart(int userId);

    List<CartResponseDto> getCartForUser(int userId);

    boolean isProductInCart(int userId, int productId);
    Status getStatusForCart(int userId);
    public int getActiveCartCountByUserId(int userId);
}
