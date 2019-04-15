package com.mobiquityinc.packer.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Shipment implements Comparable {
    private double remainingWeightLimit;
    private double totalCost;
    private List<Integer> indices = new ArrayList<>();

    public Shipment(Item item, double limit) {
        this.totalCost = item.getCost();
        this.remainingWeightLimit = limit - item.getWeight();
        this.indices.add(item.getIndex());
    }

    public Shipment(Item item, Shipment prevShipment) {
        this.totalCost = prevShipment.getTotalCost() + item.getCost();
        this.remainingWeightLimit = prevShipment.getRemainingWeightLimit() - item.getWeight();
        this.indices.addAll(prevShipment.getIndices());
        this.indices.add(item.getIndex());
    }


    @Override
    public int compareTo(Object o) {
        Shipment that = (Shipment) o;
        if (this.getTotalCost() == that.getTotalCost()) {
            return Double.compare(this.getTotalCost(), that.getTotalCost());
        }
        return Double.compare(that.getRemainingWeightLimit(), this.getRemainingWeightLimit());
    }
}
