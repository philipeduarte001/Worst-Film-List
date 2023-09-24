package com.philipe.Worst.Film.List.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CsvReader<T> implements FileReader<T> {

    final Class<T> typeParameterClass;

    public CsvReader(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public List<T> read(String path) {
        try (Reader reader = Files.newBufferedReader(Paths.get(path))) {
            return read(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<T> read(Reader reader) {
        try {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(typeParameterClass)
                    .withSeparator(';')
                    .build();

            return csvToBean.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
