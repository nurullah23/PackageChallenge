package com.mobiquityinc.packer;

import static java.util.stream.Collectors.joining;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mobiquityinc.packer.exceptions.APIException;
import com.mobiquityinc.packer.models.Package;
import com.mobiquityinc.packer.utils.PackageProcessor;
import com.mobiquityinc.packer.utils.PackageReader;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class Packer {

    public static Injector injector = Guice.createInjector(new PackerModule());

    public static String pack(String filePath) throws APIException {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }

        try {
            PackageReader packageReader = injector.getInstance(PackageReader.class);
            PackageProcessor packageProcessor = injector.getInstance(PackageProcessor.class);

            List<Package> packages = packageReader.read(filePath);

            return packages.stream()
                    .map(packageProcessor::getMostExpensiveItems)
                    .collect(joining(StringUtils.LF));
        }
        catch (Exception e) {
            throw new APIException("An exception occured while selecting packages to send", e);
        }
    }
}
