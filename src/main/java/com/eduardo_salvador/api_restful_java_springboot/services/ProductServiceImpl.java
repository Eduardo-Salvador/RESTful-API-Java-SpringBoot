package com.eduardo_salvador.api_restful_java_springboot.services;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductRecordDto;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductResponseDto;
import com.eduardo_salvador.api_restful_java_springboot.exceptions.NoFindException;
import com.eduardo_salvador.api_restful_java_springboot.mappers.ProductMapper;
import com.eduardo_salvador.api_restful_java_springboot.models.ProductModel;
import com.eduardo_salvador.api_restful_java_springboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
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
    public List<ProductResponseDto> findAll() {
        List<ProductModel> productsList = productRepository.findAll();
        if (productsList.isEmpty()) {
            throw new NoFindException();
        }
        return productsList.stream()
                .map(modelMapper::toResponseDto)
                .toList();
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