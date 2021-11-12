package org.example;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EnrollmentManagerTests {
    EnrollmentManagerObjectHandler emoH = new EnrollmentManagerObjectHandler();

    @Test
    public void loadIsNotEmpty() throws FileNotFoundException {
        List<User> listOfUsers = emoH.loadFromCSV("enrollment.csv");
        assertFalse(listOfUsers.isEmpty());
    }

    @Test
    public void mapIsNotEmpty() {
        User user1 = new User("Matthew", "Willis", "mwillis", 1, "BCBSAL");
        User user2 = new User("Matt", "Willis", "mwillis", 2, "BCBSAL");
        User user3 = new User("Billy", "Bob", "billyBob", 1, "CIGNA");
        List<User> listOfUsers = Arrays.asList(user1, user2, user3);

        Map<String, List<User>> insuranceCompanies = emoH.groupByInsuranceCompany(listOfUsers);
        assertTrue(insuranceCompanies.containsKey("BCBSAL"));
        assertEquals(2, insuranceCompanies.get("BCBSAL").size());
    }

    @Test
    public void checkdeDeuping() {
        User user1 = new User("Matthew", "Willis", "mwillis", 1, "BCBSAL");
        User user2 = new User("Matt", "Willis", "mwillis", 2, "BCBSAL");
        User user3 = new User("Billy", "Bob", "billyBob", 1, "CIGNA");
        List<User> listOfUsers = Arrays.asList(user1, user2, user3);
        Map<String, List<User>> insuranceCompanies = emoH.groupByInsuranceCompany(listOfUsers);
        Map<String, List<User>> deDuped = emoH.deduplicateUsers(insuranceCompanies);

        assertEquals(1, deDuped.get("BCBSAL").size());
        assertEquals(1, deDuped.get("CIGNA").size());
        assertEquals(2, (int) deDuped.get("BCBSAL").get(0).getVersion());
    }

    @Test
    public void checkingSorting() {
        User user1 = new User("Matthew", "Willis", "mwillis", 1, "BCBSAL");
        User user2 = new User("Matt", "Willis", "mwillis", 2, "BCBSAL");
        User user3 = new User("Billy", "Bob", "billyBob", 1, "CIGNA");
        User user4 = new User("Madeline", "Zora", "zoramay", 1, "BCBSAL");
        User user5 = new User("Madeline", "Willis", "maywillis", 1, "BCBSAL");

        List<User> listOfUsers = Arrays.asList(user1, user2, user3, user4, user5);
        Map<String, List<User>> insuranceCompanies = emoH.groupByInsuranceCompany(listOfUsers);
        Map<String, List<User>> deDuped = emoH.deduplicateUsers(insuranceCompanies);
        Map<String, List<User>> sortedUsers = emoH.sortByLastFirstName(deDuped);

        assertEquals("Willis", sortedUsers.get("BCBSAL").get(0).getLastName());
        assertEquals("Zora", sortedUsers.get("BCBSAL").get(2).getLastName());
        assertEquals("Madeline", sortedUsers.get("BCBSAL").get(0).getFirstName());
    }

}
