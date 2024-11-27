package com.web.serviceorientedweb.services.impl;

import com.web.serviceorientedweb.models.Person;
import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.repositories.PersonRepository;
import com.web.serviceorientedweb.services.PersonService;
import org.web.transportapi.dto.PersonDto;
import org.web.transportapi.dto.PersonViewDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService<UUID> {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final RaceServiceImpl raceService;

    public PersonServiceImpl(PersonRepository personRepository, ModelMapper modelMapper, RaceServiceImpl raceService) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
        this.raceService = raceService;
    }

    @Override
    public List<PersonDto> getAllPersons() {
        return personRepository.findAll().stream().map(person -> modelMapper.map(person, PersonDto.class)).collect(Collectors.toList());
    }

    @Override
    public PersonViewDto getPersonById(UUID id) {
        return modelMapper.map(personRepository.findById(id).orElse(null), PersonViewDto.class);
    }

    @Override
    public PersonViewDto createPerson(PersonViewDto person) {
        Person newPerson = modelMapper.map(person, Person.class);
        newPerson.setRace(raceService.findRaceByName(person.getRaceName()));
        return modelMapper.map(personRepository.saveAndFlush(newPerson), PersonViewDto.class);
    }

    @Override
    public void deletePerson(UUID id) {
        personRepository.deleteById(id);
    }

    @Override
    public boolean changeRace(UUID id, String raceName) {
        Person person = personRepository.findById(id).orElse(null);
        Race race = raceService.findRaceByName(raceName);
        if (raceService.getAvaibleSeatsByRaceName(raceName)>0){
            person.setRace(race);
            personRepository.saveAndFlush(person);
            return true;
        }
        return false;
    }

}

