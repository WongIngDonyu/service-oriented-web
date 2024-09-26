package com.web.serviceorientedweb.services.impl;

import com.web.serviceorientedweb.models.Person;
import com.web.serviceorientedweb.repositories.PersonRepository;
import com.web.serviceorientedweb.services.PersonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PersonServiceImpl implements PersonService<UUID> {
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {this.personRepository = personRepository;}

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public Person getPersonById(UUID id) {
        return personRepository.findById(id).orElse(null);
    }

    @Override
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public void deletePerson(UUID id) {
        personRepository.deleteById(id);

    }
}
