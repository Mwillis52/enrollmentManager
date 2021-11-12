import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class EnrollmentManagerObjectHandler {
    EnrollmentManagerFileWriter eFW = new EnrollmentManagerFileWriter();
    public Integer enrollmentManagerObjectHandler()  {

       ArrayList<User> objectsFromCSV =  loadFromCSV("enrollment.csv");
        Map<String,List<User>> insuranceCompanies = insuranceGrouper(objectsFromCSV);
        Map<String, List<User>> deDuped = userDedupe(insuranceCompanies);
        Map<String, List<User>> sortedUsers = sortByLastFirstName(deDuped);
        eFW.writeFile(sortedUsers);
        return 0;
    }

    public ArrayList<User> loadFromCSV(String fileName) {
        ArrayList<User> objectsFromCSV = new ArrayList<>();
            try {
                objectsFromCSV = (ArrayList<User>) new CsvToBeanBuilder(new FileReader(fileName))
                        .withType(User.class).build().parse();
            } catch (FileNotFoundException e) {
               System.out.println(e);
            }


        return objectsFromCSV;
    }
    public Map<String, List<User>> insuranceGrouper(List<User> fromCsv){
        Map<String, List<User>> insuranceCompanies = new HashMap<>();
        for (User user : fromCsv){
            if(insuranceCompanies.containsKey(user.getInsuranceCompany())){
                insuranceCompanies.get(user.getInsuranceCompany()).add(user);
            }else{
                ArrayList<User> userToPut = new ArrayList<>();
                userToPut.add(user);
                insuranceCompanies.put(user.getInsuranceCompany(),userToPut);
            }
        }
        return insuranceCompanies;
    }

    public Map<String, List<User>> userDedupe(Map<String,List<User>> groupedUsers){
        Map<String, List<User>> deDupedInuranceCompanies = new HashMap<String, List<User>>();
        groupedUsers.forEach((key,listOfUsers) -> {
            Collection<User> dedupUsers = listOfUsers.stream().collect(
                    Collectors.groupingBy(User::getUserId,
                            Collectors.collectingAndThen(
                                    Collectors.maxBy(Comparator.comparing(User::getVersion)),
                                    Optional::get
                            )
                )
            ).values();

            deDupedInuranceCompanies.put(key, new ArrayList<>(dedupUsers));
        });
        return deDupedInuranceCompanies;
    }

    public Map<String,List<User>> sortByLastFirstName(Map<String,List<User>> deDupedUsers){
        Map<String,List<User>> sortedUsers = new HashMap<String, List<User>>();
        Comparator<User> compareByFirstName = Comparator.comparing(User::getFirstName);
        Comparator<User> compareByLastName = Comparator.comparing(User :: getLastName);
        Comparator<User> compareByFullName = compareByLastName.thenComparing(compareByFirstName);

        deDupedUsers.forEach((key,listOfUsers) -> {
            List<User> sortedUserList = listOfUsers.stream().sorted(compareByFullName).collect(Collectors.toList());
            sortedUsers.put(key, new ArrayList<>(sortedUserList));
        });
        return sortedUsers;
    }

}
