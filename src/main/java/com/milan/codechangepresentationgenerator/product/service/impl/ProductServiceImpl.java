package com.milan.codechangepresentationgenerator.product.service.impl;


import com.milan.codechangepresentationgenerator.mapper.ProductModelMapper;
import com.milan.codechangepresentationgenerator.product.dto.ProductDto;
import com.milan.codechangepresentationgenerator.product.entity.Product;
import com.milan.codechangepresentationgenerator.product.repository.ProductRepository;
import com.milan.codechangepresentationgenerator.product.service.ProductService;
import com.milan.codechangepresentationgenerator.shared.exception.custom.ResourceNotFoundException;
import com.milan.codechangepresentationgenerator.shared.status.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductModelMapper productModelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductModelMapper productModelMapper) {
        this.productRepository = productRepository;
        this.productModelMapper = productModelMapper;
    }

    public ProductDto saveProduct(ProductDto productDto) {
        log.debug("Saving product: {}", productDto);
        Product product = productModelMapper.convertToEntity(productDto);
        Product savedProduct = productRepository.save(product);
        log.info("Product saved: {}", savedProduct);
        return productModelMapper.convertToDto(savedProduct);
    }

    @Override
    public ProductDto processProductImage(ProductDto productDto, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            productDto.setProductImage(image.getBytes());
        }
        return productDto;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findByStatus(Status.ACTIVE);
        log.info("Retrieved all products.");
        return productModelMapper.convertToDtoList(products);
    }

    @Override
    public ProductDto getProductById(int id) {
        Product product = productRepository.findByIdAndStatus(id, Status.ACTIVE).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        log.info("Retrieved product with id: {}", id);
        return productModelMapper.convertToDto(product);
    }

    @Override
    public ProductDto updateProductById(ProductDto updatedProductDetails, int id) {
        Product existingProduct = productRepository.findByIdAndStatus(id, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException("", "", id));
        // Validate and update specific fields
        if (updatedProductDetails.getProductName() != null) {
            existingProduct.setProductName(updatedProductDetails.getProductName());
        }
        if (updatedProductDetails.getProductPrice() != -1.0) {
            existingProduct.setProductPrice(updatedProductDetails.getProductPrice());
        }
        if (updatedProductDetails.getProductImage() != null) {
            existingProduct.setImage(updatedProductDetails.getProductImage());
        }
        if (updatedProductDetails.getProductDescription() != null) {
            existingProduct.setProductDescription(updatedProductDetails.getProductDescription());
        }
        productRepository.save(existingProduct);
        log.info("Updated  product with id: {}", id);
        return productModelMapper.convertToDto(existingProduct);
    }

    @Override
    public void deleteProductById(int id) {
        log.info(" product with id: {}", id);
        Product product = productRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        product.setStatus(Status.DELETED);
        productRepository.save(product);
    }

    @Override
    public List<ProductDto> getProductsByIds(List<Integer> ids) {
        List<Product> products = productRepository.findAllById(ids);
        return productModelMapper.convertToDtoList(products);
    }

    @Override
    public String getProductNameById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return product.getProductName();
    }
}
