package com.glowtique.glowtique.cart.repository;

import com.glowtique.glowtique.cart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
}
