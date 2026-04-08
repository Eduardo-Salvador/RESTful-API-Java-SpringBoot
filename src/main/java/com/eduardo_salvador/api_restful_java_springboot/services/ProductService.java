package com.eduardo_salvador.api_restful_java_springboot.services;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductRecordDto;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductResponseDto;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponseDto save(ProductRecordDto dto);
    List<ProductResponseDto> findAll();
    ProductResponseDto findById(UUID id);
    ProductResponseDto update(UUID id, ProductRecordDto dto);
    void delete(UUID id);
}