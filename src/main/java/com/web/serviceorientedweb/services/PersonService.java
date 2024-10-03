package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.models.Person;
import com.web.serviceorientedweb.services.dtos.PersonViewDto;

import java.util.List;
import java.util.UUID;

public interface PersonService <I extends UUID>{
    List<Person> getAllPersons();
    Person getPersonById(UUID id);
    PersonViewDto createPerson(PersonViewDto person);
    void deletePerson(UUID id);
    List<Person> findPersonsByPhones(List<String> phones);
}
