package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.services.dtos.PersonDto;
import com.web.serviceorientedweb.services.dtos.PersonViewDto;

import java.util.List;
import java.util.UUID;

public interface PersonService<I extends UUID> {
    List<PersonDto> getAllPersons();
    PersonViewDto getPersonById(UUID id);
    PersonViewDto createPerson(PersonViewDto person);
    void deletePerson(UUID id);
}
