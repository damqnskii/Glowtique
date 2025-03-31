package com.glowtique.glowtique.product.service;

import com.glowtique.glowtique.category.model.CategoryType;
import com.glowtique.glowtique.product.model.Product;
import com.glowtique.glowtique.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
    public List<Product> findByCategory(CategoryType category) {
        return productRepository.findByCategory(category);
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> getAllProductsByBrandId(UUID brandId) {
        return productRepository.findAllByBrandId(brandId);
    }
}
