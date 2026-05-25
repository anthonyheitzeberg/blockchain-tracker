package com.supplychain.blockchain_tracker.service;

import com.supplychain.blockchain_tracker.blockchain.Block;
import com.supplychain.blockchain_tracker.blockchain.Blockchain;
import com.supplychain.blockchain_tracker.model.ChainEvent;
import com.supplychain.blockchain_tracker.model.EventType;
import com.supplychain.blockchain_tracker.model.Product;
import com.supplychain.blockchain_tracker.repository.ChainEventRepository;
import com.supplychain.blockchain_tracker.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplyChainService {

    private final Blockchain blockchain;
    private final ProductRepository productRepository;
    private final ChainEventRepository chainEventRepository;

    @Transactional
    public Product registerProduct(String name, String serialNumber,
                                   String origin, String description) {
        if (productRepository.existsBySerialNumber(serialNumber)) {
            throw new IllegalArgumentException(
                    "Product with serial number " + serialNumber + " already exists"
            );
        }

        Product product = Product.builder()
                .name(name)
                .serialNumber(serialNumber)
                .origin(origin)
                .description(description)
                .build();

        product = productRepository.save(product);

        // Record the first block event for this product
        String blockData = buildBlockData(product, EventType.MANUFACTURED, origin, "System", "Product registered");
        Block block = blockchain.addBlock(blockData);

        ChainEvent event = ChainEvent.builder()
                .product(product)
                .eventType(EventType.MANUFACTURED)
                .location(origin)
                .handledBy("System")
                .notes("Product registered")
                .blockIndex(block.getIndex())
                .blockHash(block.getHash())
                .build();

        chainEventRepository.save(event);
        return product;
    }

    @Transactional
    public ChainEvent recordEvent(Long productId, EventType eventType,
                                  String location, String handledBy, String notes) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        String blockData = buildBlockData(product, eventType, location, handledBy, notes);
        Block block = blockchain.addBlock(blockData);

        ChainEvent event = ChainEvent.builder()
                .product(product)
                .eventType(eventType)
                .location(location)
                .handledBy(handledBy)
                .notes(notes)
                .blockIndex(block.getIndex())
                .blockHash(block.getHash())
                .build();

        return chainEventRepository.save(event);
    }

    public List<ChainEvent> getProductHistory(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
        return chainEventRepository.findByProductIdOrderByOccurredAtAsc(productId);
    }

    public List<Block> getFullChain() {
        return blockchain.getChain();
    }

    public boolean validateChain() {
        return blockchain.isChainValid();
    }

    private String buildBlockData(Product product, EventType eventType,
                                  String location, String handledBy, String notes) {
        return String.format("[%s] Product: %s (SN: %s) | Event: %s | Location: %s | By: %s | Notes: %s",
                java.time.Instant.now(), product.getName(), product.getSerialNumber(),
                eventType, location, handledBy, notes);
    }
}