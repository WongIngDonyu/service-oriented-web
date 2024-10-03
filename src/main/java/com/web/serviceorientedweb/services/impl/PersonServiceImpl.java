package com.web.serviceorientedweb.services.impl;

import com.web.serviceorientedweb.models.Person;
import com.web.serviceorientedweb.repositories.PersonRepository;
import com.web.serviceorientedweb.repositories.RaceRepository;
import com.web.serviceorientedweb.repositories.TransportRepository;
import com.web.serviceorientedweb.services.PersonService;
import com.web.serviceorientedweb.services.dtos.PersonViewDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PersonServiceImpl implements PersonService<UUID> {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final RaceServiceImpl raceService;
    public PersonServiceImpl(PersonRepository personRepository, ModelMapper modelMapper, @Lazy RaceServiceImpl raceService) {this.personRepository = personRepository; this.modelMapper = modelMapper; this.raceService = raceService;}

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public Person getPersonById(UUID id) {
        return personRepository.findById(id).orElse(null);
    }

    @Override
    public PersonViewDto createPerson(PersonViewDto person) {
        Person personEntity = modelMapper.map(person, Person.class);
        personEntity.setRace(raceService.findRaceByName(person.getRaceName()));
        personEntity = personRepository.saveAndFlush(personEntity);
        return modelMapper.map(personEntity, PersonViewDto.class);
    }

    @Override
    public void deletePerson(UUID id) {
        personRepository.deleteById(id);

    }

    @Override
    public List<Person> findPersonsByPhones(List<String> phones) {
        List<Person> foundPersons = new ArrayList<>();
        for (String phone : phones) {
            List<Person> persons = personRepository.findByPhone(phone);
            if (persons != null && !persons.isEmpty()) {
                foundPersons.addAll(persons);
            }
        }
        return foundPersons;
    }

}
