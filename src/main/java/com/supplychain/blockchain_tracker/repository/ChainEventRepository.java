package com.supplychain.blockchain_tracker.repository;

import com.supplychain.blockchain_tracker.model.ChainEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChainEventRepository extends JpaRepository<ChainEvent, Long> {
    List<ChainEvent> findByProductIdOrderByOccurredAtAsc(Long productId);
}
