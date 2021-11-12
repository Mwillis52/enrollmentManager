import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnrollmentManagerTests {
    EnrollmentManagerObjectHandler emoH = new EnrollmentManagerObjectHandler();

    @Test
    public void loadIsNotEmpty() {
        ArrayList<User> listOfUsers =  emoH.loadFromCSV("enrollment.csv");
        assertTrue(!listOfUsers.isEmpty());
    }
    @Test
    public void mapIsNotEmpty() {
        User user1 = new User("Matthew","Willis","mwillis",1,"BCBSAL");
        User user2 = new User("Matt","Willis","mwillis",2,"BCBSAL");
        User user3 = new User("Billy","Bob","billyBob",1,"CIGNA");
        List<User> listOfUsers = Arrays.asList(user1,user2,user3);

        Map<String, List<User>> insuranceCompanies = emoH.insuranceGrouper(listOfUsers);
        assertTrue(insuranceCompanies.containsKey("BCBSAL"));
        assertTrue(insuranceCompanies.get("BCBSAL").size() == 2);
    }

    @Test

    public void checkdeDeuping(){
        User user1 = new User("Matthew","Willis","mwillis",1,"BCBSAL");
        User user2 = new User("Matt","Willis","mwillis",2,"BCBSAL");
        User user3 = new User("Billy","Bob","billyBob",1,"CIGNA");
        List<User> listOfUsers = Arrays.asList(user1,user2,user3);
        Map<String, List<User>> insuranceCompanies = emoH.insuranceGrouper(listOfUsers);
        Map<String, List<User>> deDuped = emoH.userDedupe(insuranceCompanies);

        assertTrue(deDuped.get("BCBSAL").size() == 1);
        assertTrue(deDuped.get("CIGNA").size() == 1);
        assertTrue(deDuped.get("BCBSAL").get(0).getVersion() == 2);


    }

    @Test
    public void checkingSorting(){
        User user1 = new User("Matthew","Willis","mwillis",1,"BCBSAL");
        User user2 = new User("Matt","Willis","mwillis",2,"BCBSAL");
        User user3 = new User("Billy","Bob","billyBob",1,"CIGNA");
        User user4 = new User("Madeline","Zora","zoramay",1,"BCBSAL");
        User user5 = new User("Madeline","Willis","maywillis",1,"BCBSAL");

        List<User> listOfUsers = Arrays.asList(user1,user2,user3,user4,user5);
        Map<String, List<User>> insuranceCompanies = emoH.insuranceGrouper(listOfUsers);
        Map<String, List<User>> deDuped = emoH.userDedupe(insuranceCompanies);
        Map<String, List<User>> sortedUsers = emoH.sortByLastFirstName(deDuped);

        assertTrue(sortedUsers.get("BCBSAL").get(0).getLastName().equals("Willis"));
        assertTrue(sortedUsers.get("BCBSAL").get(2).getLastName().equals("Zora"));
        assertTrue(sortedUsers.get("BCBSAL").get(0).getFirstName().equals("Madeline"));

    }

}
