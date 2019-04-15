package com.mobiquityinc.packer.models;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Package {
    private final double weightLimit;
    private final List<Item> items;
}
