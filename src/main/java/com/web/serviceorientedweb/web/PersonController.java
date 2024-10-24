package com.web.serviceorientedweb.web;

import com.web.serviceorientedweb.services.PersonService;
import com.web.serviceorientedweb.services.dtos.PersonDto;
import com.web.serviceorientedweb.services.dtos.PersonViewDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;
    private final RabbitTemplate rabbitTemplate;
    private final String exchange = "persons-exchange";
    private final String routingKey = "persons-routing-key";

    @Autowired
    public PersonController(PersonService personService, RabbitTemplate rabbitTemplate) {
        this.personService = personService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/all")
    public List<EntityModel<PersonDto>> getAllPersons() {
        List<PersonDto> personDtos = personService.getAllPersons();
        List<EntityModel<PersonDto>> entityModels = new ArrayList<>();
        for (PersonDto personDto : personDtos) {
            EntityModel<PersonDto> entityModel = EntityModel.of(personDto,
                    linkTo(methodOn(PersonController.class).getPersonById(personDto.getId())).withSelfRel());
            entityModels.add(entityModel);
        }
        return entityModels;
    }

    @GetMapping("/{id}")
    public EntityModel<PersonViewDto> getPersonById(@PathVariable UUID id) {
        PersonViewDto personViewDto = personService.getPersonById(id);
        EntityModel<PersonViewDto> entityModel = EntityModel.of(personViewDto,
                linkTo(methodOn(PersonController.class).getPersonById(id)).withSelfRel(),
                linkTo(methodOn(PersonController.class).getAllPersons()).withRel("all-persons"));
        List<PersonDto> persons = personService.getAllPersons();
        int personIndex = -1;
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getId().equals(id)) {
                personIndex = i;
                break;
            }
        }
        if (personIndex < persons.size() - 1) {
            UUID nextPersonId = persons.get(personIndex + 1).getId();
            entityModel.add(linkTo(methodOn(PersonController.class).getPersonById(nextPersonId)).withRel("next"));
        }
        if (personIndex > 0) {
            UUID previousPersonId = persons.get(personIndex - 1).getId();
            entityModel.add(linkTo(methodOn(PersonController.class).getPersonById(previousPersonId)).withRel("previous"));
        }
        return entityModel;
    }

    @PostMapping
    public EntityModel<PersonDto> createPerson(@RequestBody PersonViewDto person) {
        PersonViewDto createdPerson = personService.createPerson(person);
        PersonDto personDto = new PersonDto(createdPerson.getFirstName(), createdPerson.getLastName(), createdPerson.getPhone());
        rabbitTemplate.convertAndSend(exchange, routingKey, "Person created: " + createdPerson.getId());
        EntityModel<PersonDto> entityModel = EntityModel.of(personDto,
                linkTo(methodOn(PersonController.class).getPersonById(createdPerson.getId())).withSelfRel(),
                linkTo(methodOn(PersonController.class).getAllPersons()).withRel("all-persons"));
        return entityModel;
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable UUID id) {
        personService.deletePerson(id);
        rabbitTemplate.convertAndSend(exchange, routingKey, "Person deleted: " + id);
    }
}
