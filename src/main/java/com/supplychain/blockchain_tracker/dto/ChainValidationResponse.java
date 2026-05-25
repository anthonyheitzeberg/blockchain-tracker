package com.supplychain.blockchain_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChainValidationResponse {
    private boolean valid;
    private int chainLength;
    private String message;
}
