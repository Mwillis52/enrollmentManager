package org.example;

import com.opencsv.bean.CsvBindByName;

@CsvBindByNameOrder({"User Id", "First Name", "Last Name", "Version", "Insurance Company"})
public class User {

    public User() {

    }

    public User(String firstName, String lastName, String userId, Integer version, String insuranceCompany) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.version = version;
        this.insuranceCompany = insuranceCompany;
    }

    @CsvBindByName(column = "First Name")
    private String firstName;

    @CsvBindByName(column = "Last Name")
    private String lastName;

    @CsvBindByName(column = "User Id")
    private String userId;

    @CsvBindByName(column = "Version")
    private Integer version;


    @CsvBindByName(column = "Insurance Company")
    private String insuranceCompany;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userId='" + userId + '\'' +
                ", version=" + version +
                ", insuranceCompany='" + insuranceCompany + '\'' +
                '}';
    }
}
