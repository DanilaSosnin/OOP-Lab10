package com.example.lab10.model;

import java.io.Serializable;

public class Tutor implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String subject;
    private int experience;
    private double price;
    private String phoneNumber;

    public Tutor() {
    }

    public Tutor(int id, String firstName, String lastName, String subject,
                 int experience, double price, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.subject = subject;
        this.experience = experience;
        this.price = price;
        this.phoneNumber = phoneNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}