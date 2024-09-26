package com.milan.codechangepresentationgenerator.product.service;

import com.milan.codechangepresentationgenerator.product.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductDto saveProduct(ProductDto product);

    ProductDto processProductImage(ProductDto product, MultipartFile image) throws IOException;

    List<ProductDto> getAllProducts();

    ProductDto getProductById(int id);

    ProductDto updateProductById(ProductDto updatedProductDetails, int id);

    void deleteProductById(int id);

    List<ProductDto> getProductsByIds(List<Integer> ids);


    String getProductNameById(int id);
}
