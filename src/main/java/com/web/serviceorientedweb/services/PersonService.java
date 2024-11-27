package com.web.serviceorientedweb.services;

import org.web.transportapi.dto.PersonDto;
import org.web.transportapi.dto.PersonViewDto;

import java.util.List;
import java.util.UUID;

public interface PersonService<I extends UUID> {
    List<PersonDto> getAllPersons();
    PersonViewDto getPersonById(UUID id);
    PersonViewDto createPerson(PersonViewDto person);
    void deletePerson(UUID id);
    boolean changeRace(UUID id, String raceName);
}
