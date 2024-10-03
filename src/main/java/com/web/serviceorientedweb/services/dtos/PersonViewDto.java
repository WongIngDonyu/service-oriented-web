package com.web.serviceorientedweb.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class PersonViewDto {
    @JsonIgnore
    private UUID id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String phone;
    private String raceName;
    public PersonViewDto() {}

    public PersonViewDto(String firstName, String lastName, String patronymic, String email, String phone, String raceName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.phone = phone;
        this.raceName = raceName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRaceName() {
        return raceName;
    }
    public UUID getId() {return id;}
}
