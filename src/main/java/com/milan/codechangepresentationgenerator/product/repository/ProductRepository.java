package com.milan.codechangepresentationgenerator.product.repository;

import com.milan.codechangepresentationgenerator.product.entity.Product;
import com.milan.codechangepresentationgenerator.shared.status.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByStatus(Status status);
    Optional<Product> findByIdAndStatus(Integer id, Status status);
}
