package com.milan.codechangepresentationgenerator.cart.repository;

import com.milan.codechangepresentationgenerator.cart.entity.Cart;
import com.milan.codechangepresentationgenerator.product.entity.Product;
import com.milan.codechangepresentationgenerator.shared.status.Status;
import com.milan.codechangepresentationgenerator.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {

    Optional<Cart> findByUserAndProductAndStatus(User user, Product product, Status status);

    List<Cart> findByUserAndStatus(User user, Status status);

    boolean existsByUser_IdAndProduct_IdAndStatus(int userId, int productId, Status status);

    boolean existsByUser_IdAndStatus(int userId, Status status);

    @Query("SELECT COUNT(c) FROM Cart c WHERE c.user.id = :userId AND c.status = com.milan.codechangepresentationgenerator.shared.status.Status.ACTIVE")
    int getActiveCartCountByUserId(@Param("userId") int userId);
}