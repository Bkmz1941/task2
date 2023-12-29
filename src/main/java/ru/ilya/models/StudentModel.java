package ru.ilya.models;

import com.google.gson.annotations.SerializedName;


public class StudentModel {
    private int id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("second_name")
    private String secondName;
    @SerializedName("third_name")
    private String thirdName;
    @SerializedName("birthday_at")
    private String birthdayAt;
    @SerializedName("group_name")
    private String groupName;

    public StudentModel(int id, String firstName, String secondName, String thirdName, String birthdayAt, String group) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.birthdayAt = birthdayAt;
        this.groupName = group;
    }

    @Override
    public String toString() {
        return "StudentModel{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", thirdName='" + thirdName + '\'' +
                ", birthdayAt='" + birthdayAt + '\'' +
                ", group='" + groupName + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getBirthdayAt() {
        return birthdayAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setId(int id) {
        this.id = id;
    }
}
