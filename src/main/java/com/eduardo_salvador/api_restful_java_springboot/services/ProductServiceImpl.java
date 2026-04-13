package com.eduardo_salvador.api_restful_java_springboot.services;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductRecordDto;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductResponseDto;
import com.eduardo_salvador.api_restful_java_springboot.exceptions.NoFindException;
import com.eduardo_salvador.api_restful_java_springboot.mappers.ProductMapper;
import com.eduardo_salvador.api_restful_java_springboot.models.ProductModel;
import com.eduardo_salvador.api_restful_java_springboot.repositories.ProductRepository;
import com.eduardo_salvador.api_restful_java_springboot.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper modelMapper;

    @Override
    public ProductResponseDto save(ProductRecordDto productRecordDto) {
        ProductModel productModel = modelMapper.toModel(productRecordDto);
        productRepository.save(productModel);
        return modelMapper.toResponseDto(productModel);
    }

    @Override
    public Page<ProductResponseDto> findAll(String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Specification<ProductModel> spec = Specification
                .where(ProductSpecification.hasName(name)
                        .and(ProductSpecification.hasMinPrice(minPrice))
                        .and(ProductSpecification.hasMaxPrice(maxPrice))
                );
        Page<ProductModel> productsList = productRepository.findAll(spec, pageable);
        if (productsList.isEmpty()) {
            throw new NoFindException();
        }
        return productsList.map(modelMapper::toResponseDto);
    }

    @Override
    public ProductResponseDto findById(UUID id) {
        Optional<ProductModel> product0 = productRepository.findById(id);
        if (product0.isEmpty()) {
            throw new NoFindException("Product not found.");
        }
        return modelMapper.toResponseDto(product0.get());
    }

    @Override
    public ProductResponseDto update(UUID id, ProductRecordDto productRecordDto) {
        Optional<ProductModel> product0 = productRepository.findById(id);
        if (product0.isEmpty()) {
            throw new NoFindException("Product not found.");
        }
        ProductModel productModel = product0.get();
        modelMapper.updateModel(productRecordDto, productModel);
        productRepository.save(productModel);
        return modelMapper.toResponseDto(productModel);
    }

    @Override
    public void delete(UUID id) {
        Optional<ProductModel> product0 = productRepository.findById(id);
        if (product0.isEmpty()) {
            throw new NoFindException("Product not found.");
        }
        productRepository.delete(product0.get());
    }
}