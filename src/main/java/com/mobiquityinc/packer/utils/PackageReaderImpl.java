package com.mobiquityinc.packer.utils;

import static java.util.stream.Collectors.toList;

import com.mobiquityinc.packer.exceptions.APIException;
import com.mobiquityinc.packer.models.Item;
import com.mobiquityinc.packer.models.Package;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public class PackageReaderImpl implements PackageReader {

    private static final String LIMIT_SEPARATOR = " : ";
    private static final String WHITESPACE_REGEX = "\\s+";
    private static final String ITEM_REGEX = "^\\(([0-9]*),([0-9]*\\.?[0-9]*),€([0-9]*\\.?[0-9]*)\\)$";

    public List<Package> read(String path) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            return br.lines().map(this::convertLineToPackage).collect(toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Package convertLineToPackage(String line) {
        String[] parts = line.split(LIMIT_SEPARATOR);
        double limit = Double.valueOf(parts[0]);
        String[] items = parts[1].split(WHITESPACE_REGEX);
        return Package.builder()
                .weightLimit(limit)
                .items(Stream.of(items)
                        .filter(StringUtils::isNotBlank)
                        .map(this::convertStringToItem)
                        .collect(toList())
                )
                .build();
    }

    private Item convertStringToItem(String itemStr) {
        Pattern pattern = Pattern.compile(ITEM_REGEX);
        Matcher matcher = pattern.matcher(itemStr);
        if (matcher.find()) {
            int index = Integer.valueOf(matcher.group(1));
            double weight = Double.valueOf(matcher.group(2));
            double cost = Double.valueOf(matcher.group(3));
            return Item.builder()
                    .index(index)
                    .weight(weight)
                    .cost(cost)
                    .build();
        }
        throw new RuntimeException("Invalid item ");
    }
}
