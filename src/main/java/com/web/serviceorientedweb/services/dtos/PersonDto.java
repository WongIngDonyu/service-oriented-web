package com.web.serviceorientedweb.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class PersonDto {
    @JsonIgnore
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    public PersonDto() {}

    public PersonDto(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public UUID getId() {
        return id;
    }
}
