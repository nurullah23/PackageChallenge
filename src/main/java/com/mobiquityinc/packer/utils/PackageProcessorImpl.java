package com.mobiquityinc.packer.utils;

import static java.util.stream.Collectors.joining;

import com.mobiquityinc.packer.models.Item;
import com.mobiquityinc.packer.models.Package;
import com.mobiquityinc.packer.models.Shipment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PackageProcessorImpl implements PackageProcessor {

    private static final String DASH_CHARACTER = "-";

    @Override
    public String getMostExpensiveItems(Package pack) {
        List<Item> items = Optional.ofNullable(pack.getItems()).orElse(Collections.emptyList());

        List<Shipment> shipments = new ArrayList<>();
        double limit = pack.getWeightLimit();
        for(Item item : items) {
            if (item.getWeight() <= limit) {
                int size = shipments.size();
                shipments.add(new Shipment(item, limit));
                for(int i = 0; i < size; i++) {
                    Shipment shipment = shipments.get(i);
                    if (item.getWeight() <= shipment.getRemainingWeightLimit()) {
                        shipments.add(new Shipment(item, shipment));
                    }
                }
            }
        }

        List<Shipment> asd = shipments.stream()
                .sorted((item1, item2) -> {
                    if (item1.getTotalCost() == item2.getTotalCost()) {
                        return Double.compare(item2.getRemainingWeightLimit(), item1.getRemainingWeightLimit());
                    }
                    return Double.compare(item1.getTotalCost(), item2.getTotalCost());
                })
                .collect(Collectors.toList());

        List<Integer> selection = shipments.stream()
                .max((item1, item2) -> {
                    if (item1.getTotalCost() == item2.getTotalCost()) {
                        return Double.compare(item2.getRemainingWeightLimit(), item1.getRemainingWeightLimit());
                    }
                    return Double.compare(item1.getTotalCost(), item2.getTotalCost());
                })
                .map(Shipment::getIndices)
                .orElse(Collections.emptyList());

        return selection.size() == 0
                ? DASH_CHARACTER
                : selection.stream()
                        .map(String::valueOf)
                        .collect(joining(","));
    }
}
