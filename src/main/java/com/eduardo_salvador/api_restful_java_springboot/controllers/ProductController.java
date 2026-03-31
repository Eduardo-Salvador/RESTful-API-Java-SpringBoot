package com.eduardo_salvador.api_restful_java_springboot.controllers;
import com.eduardo_salvador.api_restful_java_springboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;


}
