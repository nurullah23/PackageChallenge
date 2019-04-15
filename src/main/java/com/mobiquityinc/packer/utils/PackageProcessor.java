package com.mobiquityinc.packer.utils;

import com.mobiquityinc.packer.models.Package;

public interface PackageProcessor {

    String getMostExpensiveItems(Package pack);
}
