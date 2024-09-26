package com.web.serviceorientedweb.web;

import com.web.serviceorientedweb.models.Person;
import com.web.serviceorientedweb.services.PersonService;
import com.web.serviceorientedweb.services.dtos.PersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService) {this.personService = personService;}

    @GetMapping("/all")
    public List<EntityModel<PersonDto>> getAllPersons() {
        List<Person> persons = personService.getAllPersons();
        List<EntityModel<PersonDto>> personDtos = new ArrayList<>();
        for (Person person : persons) {
            PersonDto personDto = new PersonDto(person.getFirstName(), person.getLastName(), person.getPhone());
            EntityModel<PersonDto> resource = EntityModel.of(personDto,
                    linkTo(methodOn(PersonController.class).getPersonById(person.getId())).withSelfRel());
            personDtos.add(resource);
        }
        return personDtos;
    }

    @GetMapping("/{id}")
    public EntityModel<PersonDto> getPersonById(@PathVariable UUID id) {
        Person person = personService.getPersonById(id);
        PersonDto personDto = new PersonDto(person.getFirstName(), person.getLastName(), person.getPhone());
        EntityModel<PersonDto> resource = EntityModel.of(personDto,
                linkTo(methodOn(PersonController.class).getPersonById(person.getId())).withSelfRel(),
                linkTo(methodOn(PersonController.class).getAllPersons()).withRel("all-persons"));
        List<Person> persons = personService.getAllPersons();
        int personIndex = persons.indexOf(person);
        if (personIndex < persons.size() - 1) {
            resource.add(linkTo(methodOn(PersonController.class).getPersonById(persons.get(personIndex + 1).getId())).withRel("next"));
        }
        if (personIndex > 0) {
            resource.add(linkTo(methodOn(PersonController.class).getPersonById(persons.get(personIndex - 1).getId())).withRel("previous"));
        }
        return resource;
    }

    @PostMapping
    public ResponseEntity<EntityModel<PersonDto>> createPerson(@RequestBody Person person) {
        Person createdPerson = personService.createPerson(person);
        PersonDto personDto = new PersonDto(createdPerson.getFirstName(), createdPerson.getLastName(), createdPerson.getPhone());
        EntityModel<PersonDto> resource = EntityModel.of(personDto,
                linkTo(methodOn(PersonController.class).getPersonById(createdPerson.getId())).withSelfRel(),
                linkTo(methodOn(PersonController.class).getAllPersons()).withRel("all-persons"));
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public EntityModel<String> deleteRace(@PathVariable UUID id) {
        personService.deletePerson(id);
        EntityModel<String> resource = EntityModel.of("Percon with ID " + id + " was deleted",
                linkTo(methodOn(RaceController.class).getAllRaces()).withRel("all-persons"));
        return resource;
    }
}