package com.milan.codechangepresentationgenerator.mapper;

import com.milan.codechangepresentationgenerator.product.dto.ProductDto;
import com.milan.codechangepresentationgenerator.product.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductModelMapper {
    private final ModelMapper modelMapper;
    public ProductModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
    }
    public Product convertToEntity(ProductDto productDto) {
        log.debug("Converting ProductDto to Product: {}", productDto);
        return modelMapper.map(productDto, Product.class);
    }
    public ProductDto convertToDto(Product product) {
        log.debug("Converting Product to ProductDto: {}", product);
        return modelMapper.map(product, ProductDto.class);
    }
    public List<ProductDto> convertToDtoList(List<Product> products) {
        log.debug("Converting list of Product entities to ProductDto list.");
        return products.stream()
                .sorted(Comparator.comparing(Product::getCreatedDate).reversed())
                .map(this::convertToDto).collect(Collectors.toList());
    }
}