package com.bestmatch.repository;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvLoader {

    public <T> List<T> loadCsv(String fileName, Class<T> type) {
        List<T> resultList = new ArrayList<>();
        Resource resource = new ClassPathResource(fileName);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                T instance = type.getDeclaredConstructor().newInstance();
                Field[] declaredFields = type.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    if (i < declaredFields.length) {
                        Field field = declaredFields[i];
                        field.setAccessible(true);
                        setFieldValue(instance, field, fields[i]);
                    }
                }
                resultList.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private <T> void setFieldValue(T instance, Field field, String value) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (type == int.class || type == Integer.class) {
            field.setInt(instance, Integer.parseInt(value));
        } else if (type == double.class || type == Double.class) {
            field.setDouble(instance, Double.parseDouble(value));
        } else {
            field.set(instance, value);
        }
    }
}
