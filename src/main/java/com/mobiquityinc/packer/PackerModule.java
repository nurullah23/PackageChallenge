package com.mobiquityinc.packer;

import com.google.inject.AbstractModule;
import com.mobiquityinc.packer.utils.PackageProcessor;
import com.mobiquityinc.packer.utils.PackageProcessorImpl;
import com.mobiquityinc.packer.utils.PackageReader;
import com.mobiquityinc.packer.utils.PackageReaderImpl;

class PackerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PackageReader.class).to(PackageReaderImpl.class);
        bind(PackageProcessor.class).to(PackageProcessorImpl.class);
    }
}
