package com.supplychain.blockchain_tracker.repository;

import com.supplychain.blockchain_tracker.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySerialNumber(String serialNumber);
    boolean existsBySerialNumber(String serialNumber);
}
