package com.nick.productservice.controller;

import com.nick.productservice.dto.ProductRequest;
import com.nick.productservice.dto.ProductResponse;
import com.nick.productservice.exception.ProductCreationException;
import com.nick.productservice.model.Product;
import com.nick.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestController
@RequestMapping("/api/product-service")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(value = "create")
    public ResponseEntity<Map<String, String>> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            productService.createProduct(productRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Product created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ProductCreationException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Product creation failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(value = "getallproduct")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProduct() {
        return productService.getAllProducts();
    }

    @ExceptionHandler(ProductCreationException.class)
    public ResponseEntity<Map<String, String>> handleProductCreationException(ProductCreationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Product creation failed");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
