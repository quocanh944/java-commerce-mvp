package com.example.midterm.repos;

import com.example.midterm.model.Brand;
import com.example.midterm.model.Category;
import com.example.midterm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findAllByHot(boolean isHot);
}
