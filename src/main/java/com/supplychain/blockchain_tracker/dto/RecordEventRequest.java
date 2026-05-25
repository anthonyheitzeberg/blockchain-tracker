package com.supplychain.blockchain_tracker.dto;

import com.supplychain.blockchain_tracker.model.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecordEventRequest {

    @NotNull(message = "Event type is required")
    private EventType eventType;

    @NotBlank(message = "Location is required")
    private String location;

    private String handledBy;
    private String notes;
}
