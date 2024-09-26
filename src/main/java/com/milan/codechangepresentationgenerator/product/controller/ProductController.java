package com.milan.codechangepresentationgenerator.product.controller;

import com.milan.codechangepresentationgenerator.product.dto.ProductDto;
import com.milan.codechangepresentationgenerator.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/get-product-name/{id}")
    public String getProductNameById(@PathVariable int id) {
        return productService.getProductNameById(id);
    }
    @PostMapping("/save-product")
    public ProductDto createProduct(@ModelAttribute ProductDto productDto, @RequestParam("image") MultipartFile image) throws IOException {
        productService.processProductImage(productDto, image);
        return productService.saveProduct(productDto);
    }
    @GetMapping("/get-product/{id}")
    public ProductDto getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }
    @GetMapping("/get-product-by-ids")
    public List<ProductDto> getProductsByIds(@RequestParam List<Integer> ids) {
        return productService.getProductsByIds(ids);
    }
    @PutMapping("/update/{id}")
    public ProductDto updateProductById(@PathVariable int id, @RequestBody ProductDto updatedProductDetails) {
        return productService.updateProductById(updatedProductDetails, id);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable int id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("get-all-product")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
