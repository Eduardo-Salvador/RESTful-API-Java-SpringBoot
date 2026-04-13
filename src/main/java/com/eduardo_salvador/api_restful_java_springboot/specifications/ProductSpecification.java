package com.eduardo_salvador.api_restful_java_springboot.specifications;
import com.eduardo_salvador.api_restful_java_springboot.models.ProductModel;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<ProductModel> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<ProductModel> hasMinPrice(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) ->
                minPrice == null ? null : criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"), minPrice
                );
    }

    public static Specification<ProductModel> hasMaxPrice(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                maxPrice == null ? null : criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"), maxPrice
                );
    }
}