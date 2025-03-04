package com.glowtique.glowtique.product.model;

import com.glowtique.glowtique.brand.model.Brand;
import com.glowtique.glowtique.category.model.Category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Brand brand;

    @Column(nullable = false)
    private BigDecimal price;

    private BigDecimal discountPrice;

    @Column(nullable = false)
    private int quantity;

    private String ingredients;

    @Column(length = 750)
    private String description;

    private int volume;

    @Enumerated(EnumType.STRING)
    private ProductGender productGender;

    private String image;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    private Category category;
}
