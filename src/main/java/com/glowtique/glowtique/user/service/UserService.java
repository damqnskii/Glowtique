package com.glowtique.glowtique.user.service;

import com.glowtique.glowtique.cart.model.Cart;
import com.glowtique.glowtique.cart.service.CartService;
import com.glowtique.glowtique.exception.AlreadyRegEmailException;
import com.glowtique.glowtique.exception.ExistingPhoneNumber;
import com.glowtique.glowtique.exception.UserNotExisting;
import com.glowtique.glowtique.user.model.UserRole;
import com.glowtique.glowtique.user.repository.UserRepository;
import com.glowtique.glowtique.web.dto.AdminRequest;
import com.glowtique.glowtique.web.dto.EditProfileRequest;
import com.glowtique.glowtique.web.dto.LoginRequest;
import com.glowtique.glowtique.web.dto.RegisterRequest;
import com.glowtique.glowtique.wishlistitem.model.WishlistItem;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.glowtique.glowtique.user.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CartService cartService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotExisting("User not found"));
    }


    public User login (LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findUserByEmail(loginRequest.getEmail());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return user;

    }

    @Transactional
    public User register (RegisterRequest registerRequest) {
        Optional<User> optionalUser = userRepository.findUserByEmail(registerRequest.getEmail());

        if (optionalUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }
        if (!registerRequest.password.equals(registerRequest.confirmPassword)) {
            throw new RuntimeException("Password does not match");
        }

        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .birthday(registerRequest.getBirthday())
                .gender(registerRequest.getGender())
                .phoneNumber(null)
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .town(null)
                .street(null)
                .country(null)
                .orders(new HashSet<>())
                .wishlistItems(new HashSet<>())
                .isActive(true)
                .build();

        Cart cart = cartService.createCart(user);
        user.setCart(cart);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser (UUID userId, EditProfileRequest editProfileRequest) {
        User user = getUserById(userId);
        Optional<User> isFigured = userRepository.findUserByEmail(editProfileRequest.getEmail());

        if (isFigured.isPresent() && !isFigured.get().getId().equals(userId)) {
            throw new AlreadyRegEmailException("The email address already exists");
        }
        Optional<User> isNumberRegistered = userRepository.findUserByPhoneNumber(editProfileRequest.getPhoneNumber());
        if (isNumberRegistered.isPresent() && !isNumberRegistered.get().getId().equals(userId)) {
            throw new ExistingPhoneNumber("The phone number already exists");
        }

        user.setEmail(editProfileRequest.getEmail());
        user.setFirstName(editProfileRequest.getFirstName());
        user.setLastName(editProfileRequest.getLastName());
        user.setPhoneNumber(editProfileRequest.getPhoneNumber());
        user.setCountry(editProfileRequest.getCountry());
        user.setTown(editProfileRequest.getTown());
        user.setStreet(editProfileRequest.getStreet());
        user.setFactureAddress(editProfileRequest.getFactureAddress());
        user.setShippingAddress(editProfileRequest.getShippingAddress());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }


    public void updateRole(AdminRequest adminRequest, UUID userId) {
        User user = getUserById(userId);

        user.setRole(adminRequest.getRole());

        userRepository.save(user);
    }
}
