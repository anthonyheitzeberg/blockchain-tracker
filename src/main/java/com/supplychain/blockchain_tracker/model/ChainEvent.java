package com.supplychain.blockchain_tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChainEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    @Column(nullable = false)
    private String location;

    private String handledBy;

    private String notes;

    @Column(nullable = false)
    private Instant occurredAt;

    @Column(nullable = false)
    private int blockIndex;

    @Column(nullable = false)
    private String blockHash;

    @PrePersist
    public void prePersist() {
        this.occurredAt = Instant.now();
    }
}
