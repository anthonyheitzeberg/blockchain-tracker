package com.supplychain.blockchain_tracker.controller;

import com.supplychain.blockchain_tracker.dto.RecordEventRequest;
import com.supplychain.blockchain_tracker.dto.RegisterProductRequest;
import com.supplychain.blockchain_tracker.model.ChainEvent;
import com.supplychain.blockchain_tracker.model.Product;
import com.supplychain.blockchain_tracker.service.SupplyChainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
class ProductController {

    public final SupplyChainService supplyChainService;

    @PostMapping
    public ResponseEntity<Product> registerProduct(@Valid @RequestBody RegisterProductRequest request) {
        Product product = supplyChainService.registerProduct(
                request.getName(),
                request.getSerialNumber(),
                request.getOrigin(),
                request.getDescription()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PostMapping("/{productId}/events")
    public ResponseEntity<ChainEvent> recordEvent(
            @PathVariable Long productId,
            @Valid @RequestBody RecordEventRequest request) {
        ChainEvent event = supplyChainService.recordEvent(
                productId,
                request.getEventType(),
                request.getLocation(),
                request.getHandledBy(),
                request.getNotes()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @GetMapping("/{productId}/history")
    public ResponseEntity<List<ChainEvent>> getProductHistory(@PathVariable Long productId) {
        return ResponseEntity.ok(supplyChainService.getProductHistory(productId));
    }
}
