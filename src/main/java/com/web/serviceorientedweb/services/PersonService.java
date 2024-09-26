package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.models.Person;

import java.util.List;
import java.util.UUID;

public interface PersonService <I extends UUID>{
    List<Person> getAllPersons();
    Person getPersonById(UUID id);
    Person createPerson(Person person);
    void deletePerson(UUID id);
}
