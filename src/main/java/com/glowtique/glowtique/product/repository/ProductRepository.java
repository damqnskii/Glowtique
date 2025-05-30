package com.glowtique.glowtique.product.repository;

import com.glowtique.glowtique.category.model.CategoryType;
import com.glowtique.glowtique.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategory(CategoryType category);
    Optional<Product> findById(UUID id);
    List<Product> findByName(String name);
    List<Product> findAllByBrandId(UUID id);
}
