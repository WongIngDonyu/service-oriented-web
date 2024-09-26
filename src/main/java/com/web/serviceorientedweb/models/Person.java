package com.web.serviceorientedweb.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "persons")
public class Person extends BaseEnity {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String phone;
    private Race race;

    public Person() {}

    public Person(String firstName, String lastName, String patronymic, String email, String phone, Race race) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.phone = phone;
        this.race = race;
    }

    @Column(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "patronymic")
    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    @Column(name = "email", unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "phone", unique = true)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @ManyToOne
    @JoinColumn(name = "race_id")
    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}

