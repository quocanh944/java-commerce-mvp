package com.example.midterm.repos;

import com.example.midterm.model.CartItem;
import com.example.midterm.model.Product;
import com.example.midterm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProductAndUser(Product product, User user);

    List<CartItem> findAllByUser(User user);

    void deleteByProductAndUser(Product product, User user);
}
