package com.mobiquityinc.packer;

import static org.hamcrest.CoreMatchers.nullValue;

import com.mobiquityinc.packer.exceptions.APIException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public class PackerTest {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void givenEmptyPath_whenPack_thenReturnNull() throws APIException {
        //Given
        String path = "";

        //When
        String result = Packer.pack(path);

        //Then
        collector.checkThat(result, nullValue());
    }
}
