package com.milan.codechangepresentationgenerator.cart.service.impl;
import com.milan.codechangepresentationgenerator.cart.dto.CartDto;
import com.milan.codechangepresentationgenerator.cart.dto.CartResponseDto;
import com.milan.codechangepresentationgenerator.cart.entity.Cart;
import com.milan.codechangepresentationgenerator.cart.repository.CartRepository;
import com.milan.codechangepresentationgenerator.cart.service.CartService;
import com.milan.codechangepresentationgenerator.product.entity.Product;
import com.milan.codechangepresentationgenerator.product.repository.ProductRepository;
import com.milan.codechangepresentationgenerator.shared.exception.ResourceNotFoundException;
import com.milan.codechangepresentationgenerator.shared.status.Status;
import com.milan.codechangepresentationgenerator.user.entity.User;
import com.milan.codechangepresentationgenerator.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public void addItemToCart(CartDto cartDto) {
        int userId = cartDto.getUserId();
        int productId = cartDto.getProductId();
        User user = getUserNotFound(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found", "", productId));
        Cart existingCartItem = cartRepository.findByUserAndProductAndStatus(user, product, Status.ACTIVE)
                .orElse(null);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity());
        } else {
            Cart newCartItem = modelMapper.map(cartDto, Cart.class);
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setStatus(Status.ACTIVE);
            cartRepository.save(newCartItem);
        }
    }

    private User getUserNotFound(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", "", userId));
    }

    @Override
    public void updateCartItem(CartDto cartDto) {
        int userId = cartDto.getUserId();
        int productId = cartDto.getProductId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", "", userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found", "", productId));

        Cart existingCartItem = cartRepository.findByUserAndProductAndStatus(user, product, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found", "", cartDto));

        // ModelMapper to update the existingCartItem
        modelMapper.map(cartDto, existingCartItem);

        cartRepository.save(existingCartItem);
    }

    @Override
    public void removeCartItem(int id) {
        Cart existingCartItem = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found", "", ""));

        existingCartItem.setStatus(Status.DELETED);
        cartRepository.save(existingCartItem);

        log.info("Marked cart item with id={} as DELETED", id);
    }

    @Override
    public void clearCart(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", "", ""));
        List<Cart> userCartItems = cartRepository.findByUserAndStatus(user, Status.ACTIVE);
        for (Cart cartItem : userCartItems) {
            cartItem.setStatus(Status.DELETED);
        }
        cartRepository.saveAll(userCartItems);
    }
    @Override
    public Status getStatusForCart(int userId) {
        return cartRepository.existsByUser_IdAndStatus(userId, Status.ACTIVE) ? Status.ACTIVE : Status.INACTIVE;
    }
    @Override
    public List<CartResponseDto> getCartForUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", "", userId));
        List<Cart> cartItems = cartRepository.findByUserAndStatus(user, Status.ACTIVE);
        List<CartResponseDto> cartDtoList = cartItems.stream()
                .map(cart -> modelMapper.map(cart, CartResponseDto.class))
                .collect(Collectors.toList());
        return cartDtoList;
    }
    @Override
    public boolean isProductInCart(int userId, int productId) {
        Status currentStatus = getStatusForCart(userId);
        boolean productInCart = cartRepository.existsByUser_IdAndProduct_IdAndStatus(userId, productId, currentStatus);

        if (productInCart) {
            return true;
        }
        return false;
    }
    @Override
    public int getActiveCartCountByUserId(int userId) {
        return cartRepository.getActiveCartCountByUserId(userId);
    }
}
