package com.eduardo_salvador.api_restful_java_springboot.mappers;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductRecordDto;
import com.eduardo_salvador.api_restful_java_springboot.models.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductModel toModel(ProductRecordDto dto);
    ProductRecordDto toDto(ProductModel model);
    void updateModel(ProductRecordDto dto, @MappingTarget ProductModel model);
}