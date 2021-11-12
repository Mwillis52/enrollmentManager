package org.example;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class EnrollmentManagerFileWriter {
    public void writeFile(Map<String, List<User>> sortedUsers) {
        sortedUsers.forEach((key, listOfUsers) -> {
            Writer writer;
            try {
                writer = new FileWriter(key + ".csv");
                StatefulBeanToCsv<User> csvWriter = new StatefulBeanToCsvBuilder<User>(writer)
                        .withApplyQuotesToAll(false)
                        .withMappingStrategy(new HeaderColumnNameAndOrderMappingStrategy<>(User.class))
                        .build();
                csvWriter.write(listOfUsers);
                writer.close();
            } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
                e.printStackTrace();
            }

        });
    }
}
