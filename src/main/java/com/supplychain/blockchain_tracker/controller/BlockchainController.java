package com.supplychain.blockchain_tracker.controller;

import com.supplychain.blockchain_tracker.blockchain.Block;
import com.supplychain.blockchain_tracker.dto.ChainValidationResponse;
import com.supplychain.blockchain_tracker.service.SupplyChainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/api/blockchain")
@RequiredArgsConstructor
public class BlockchainController {

    private final SupplyChainService supplyChainService;

    @GetMapping("/chain")
    public ResponseEntity<List<Block>> getFullChain() {
        return ResponseEntity.ok(supplyChainService.getFullChain());
    }

    @GetMapping("/validate")
    public ResponseEntity<ChainValidationResponse> validateChain() {
        boolean isValid = supplyChainService.validateChain();
        int length = supplyChainService.getFullChain().size();
        String message = isValid
                ? "Chain is intact - no tampering detected"
                : "⚠️ Chain integrity compromised — tampering detected!";
        return ResponseEntity.ok(new ChainValidationResponse(isValid, length, message));
    }
}
