package com.mobiquityinc.packer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.mobiquityinc.packer.exceptions.APIException;
import com.mobiquityinc.packer.models.Package;
import com.mobiquityinc.packer.utils.PackageProcessor;
import com.mobiquityinc.packer.utils.PackageReader;
import java.util.Arrays;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PackerTest {

    private static final String DASH_CHARACTER = "-";
    private static final String TEST_PATH = "some/path";
    private static final String INVALID_PATH = "invalid/path";
    private static final String ERROR_MESSAGE = "An exception occured while selecting packages to send";
    private static final String RESULT1 = "test1";
    private static final String RESULT2 = "test2";
    private static final String RESULT = "test1\ntest2";


    @Mock
    private PackageReader packageReader;

    @Mock
    private PackageProcessor packageProcessor;

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Module testModule = Modules.override(new PackerModule())
                .with(new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind(PackageReader.class).toInstance(packageReader);
                        bind(PackageProcessor.class).toInstance(packageProcessor);
                    }

                });

        Packer.injector = Guice.createInjector(testModule);
    }

    @Test
    public void givenEmptyFilePath_whenPack_thenReturnNull() throws APIException {
        //Given
        String path = StringUtils.EMPTY;

        //When
        String result = Packer.pack(path);

        //Then
        collector.checkThat(result, nullValue());
    }

    @Test
    public void givenFilePath_whenPack_thenReturnSelectedPack() throws APIException {
        //Given
        doReturn(Collections.emptyList()).when(packageReader).read(anyString());

        //When
        String result = Packer.pack(TEST_PATH);

        //Then
        verify(packageReader).read(TEST_PATH);
        collector.checkThat(result, notNullValue());
    }

    @Test
    public void givenInvalidPath_whenPack_thenThrowAPIException() throws APIException {
        //Given
        doThrow(new RuntimeException()).when(packageReader).read(anyString());

        //Then
        thrown.expect(APIException.class);
        thrown.expectMessage(ERROR_MESSAGE);

        //When
        Packer.pack(INVALID_PATH);

        //Then
        verify(packageReader).read(INVALID_PATH);
    }

    @Test
    public void givenFileWithNoContent_whenPack_thenReturnEmptyString() throws APIException {
        //Given
        doReturn(Collections.emptyList()).when(packageReader).read(anyString());

        //When
        String result = Packer.pack(TEST_PATH);

        //Then
        verify(packageReader).read(TEST_PATH);
        collector.checkThat(result, equalTo(StringUtils.EMPTY));
    }

    @Test
    public void givenPackages_whenPack_thenSeparateResultsWithLineBreak() throws APIException {
        //Given
        Package package1 = Package.builder().build();
        Package package2 = Package.builder().build();
        doReturn(Arrays.asList(package1, package2)).when(packageReader).read(anyString());
        when(packageProcessor.getMostExpensiveItems(any(Package.class)))
                .thenReturn(RESULT1)
                .thenReturn(RESULT2);

        //When
        String result = Packer.pack(TEST_PATH);

        //Then
        verify(packageReader).read(TEST_PATH);
        verify(packageProcessor, times(2)).getMostExpensiveItems(any(Package.class));
        collector.checkThat(result, equalTo(RESULT));
    }
}
