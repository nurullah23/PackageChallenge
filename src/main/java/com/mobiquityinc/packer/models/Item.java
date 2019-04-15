package com.mobiquityinc.packer.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
    private final int index;
    private final double weight;
    private final double cost;
}
