package com.mobiquityinc.packer.utils;

import com.mobiquityinc.packer.models.Package;
import java.util.List;

public interface PackageReader {
    List<Package> read(String path);
}
