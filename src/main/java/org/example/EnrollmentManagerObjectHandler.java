package org.example;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnrollmentManagerObjectHandler {

    EnrollmentManagerFileWriter enrollmentManagerFileWriter = new EnrollmentManagerFileWriter();

    public void enrollmentManagerObjectHandler() {
        List<User> objectsFromCSV;
        try {
            objectsFromCSV = (List<User>) new CsvToBeanBuilder(new FileReader("enrollment.csv"))
                    .withType(User.class).build().parse();
            Map<String, List<User>> groupedByInsuranceCompany = groupByInsuranceCompany(objectsFromCSV);
            Map<String, List<User>> deDuped = deduplicateUsers(groupedByInsuranceCompany);
            Map<String, List<User>> sortedUsers = sortByLastFirstName(deDuped);
            enrollmentManagerFileWriter.writeFile(sortedUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> loadFromCSV(String fileName) throws FileNotFoundException {
        return (ArrayList<User>) new CsvToBeanBuilder(new FileReader(fileName))
                    .withType(User.class).build().parse();
    }

    public Map<String, List<User>> groupByInsuranceCompany(List<User> users) {
        Map<String, List<User>> insuranceCompanies = new HashMap<>();
        for (User user : users) {
            if (insuranceCompanies.containsKey(user.getInsuranceCompany())) {
                insuranceCompanies.get(user.getInsuranceCompany()).add(user);
            } else {
                List<User> userToPut = new ArrayList<>();
                userToPut.add(user);
                insuranceCompanies.put(user.getInsuranceCompany(), userToPut);
            }
        }
        return insuranceCompanies;
    }

    public Map<String, List<User>> deduplicateUsers(Map<String, List<User>> groupedUsers) {
        Map<String, List<User>> mapWithoutDuplicates = new HashMap<>();
        for (Map.Entry<String, List<User>> entry : groupedUsers.entrySet()) {
            Collection<User> usersWithoutDuplicates = entry.getValue().stream()
                    .collect(Collectors.toMap(
                            User::getUserId,
                            Function.identity(),
                            BinaryOperator.maxBy(Comparator.comparing(User::getVersion))))
                    .values();

            mapWithoutDuplicates.put(entry.getKey(), new ArrayList<>(usersWithoutDuplicates));
        }
        return mapWithoutDuplicates;
    }

    public Map<String, List<User>> sortByLastFirstName(Map<String, List<User>> deDupedUsers) {
        Map<String, List<User>> sortedUsers = new HashMap<>();
        Comparator<User> compareByFirstName = Comparator.comparing(User::getFirstName);
        Comparator<User> compareByLastName = Comparator.comparing(User::getLastName);
        Comparator<User> compareByFullName = compareByLastName.thenComparing(compareByFirstName);

        deDupedUsers.forEach((key, listOfUsers) -> {
            List<User> sortedUserList = listOfUsers.stream().sorted(compareByFullName).collect(Collectors.toList());
            sortedUsers.put(key, new ArrayList<>(sortedUserList));
        });
        return sortedUsers;
    }

}
