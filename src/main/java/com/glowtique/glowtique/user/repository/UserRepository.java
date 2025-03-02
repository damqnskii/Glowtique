package com.glowtique.glowtique.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.glowtique.glowtique.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Override
    Optional<User> findById(UUID uuid);

    Optional<User> findUserByEmail(String email);

    List<User> findAll();
}
