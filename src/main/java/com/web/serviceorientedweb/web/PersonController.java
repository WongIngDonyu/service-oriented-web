package com.web.serviceorientedweb.web;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.web.transportapi.controllers.PersonApi;
import org.web.transportapi.dto.PersonDto;
import org.web.transportapi.dto.PersonViewDto;
import com.web.serviceorientedweb.services.PersonService;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PersonController implements PersonApi {

    private final PersonService personService;
    private final RabbitTemplate rabbitTemplate;
    private final ModelMapper modelMapper;
    private final String exchange = "persons-exchange";
    private final String routingKey = "persons-routing-key";

    @Autowired
    public PersonController(PersonService personService, RabbitTemplate rabbitTemplate, ModelMapper modelMapper) {
        this.personService = personService;
        this.rabbitTemplate = rabbitTemplate;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<EntityModel<PersonDto>> getAllPersons() {
        List<com.web.serviceorientedweb.services.dtos.PersonDto> servicePersonDtos = personService.getAllPersons();
        List<EntityModel<PersonDto>> personModels = new ArrayList<>();
        for (com.web.serviceorientedweb.services.dtos.PersonDto serviceDto : servicePersonDtos) {
            PersonDto personDto = modelMapper.map(serviceDto, PersonDto.class);
            EntityModel<PersonDto> entityModel = EntityModel.of(personDto,
                    linkTo(methodOn(PersonController.class).getPersonById(serviceDto.getId())).withSelfRel(),
                    linkTo(methodOn(PersonController.class).getAllPersons()).withRel("all-persons"));
            personModels.add(entityModel);
        }
        return personModels;
    }

    @Override
    public EntityModel<PersonViewDto> getPersonById(UUID id) {
        com.web.serviceorientedweb.services.dtos.PersonViewDto servicePersonViewDto = personService.getPersonById(id);
        PersonViewDto personViewDto = modelMapper.map(servicePersonViewDto, PersonViewDto.class);
        List<com.web.serviceorientedweb.services.dtos.PersonDto> allPersons = personService.getAllPersons();
        int currentIndex = -1;
        for (int i = 0; i < allPersons.size(); i++) {
            if (allPersons.get(i).getId().equals(id)) {
                currentIndex = i;
                break;
            }
        }

        EntityModel<PersonViewDto> entityModel = EntityModel.of(personViewDto,
                linkTo(methodOn(PersonController.class).getPersonById(id)).withSelfRel(),
                linkTo(methodOn(PersonController.class).getAllPersons()).withRel("all-persons"));

        if (currentIndex > 0) {
            UUID previousId = allPersons.get(currentIndex - 1).getId();
            entityModel.add(linkTo(methodOn(PersonController.class).getPersonById(previousId)).withRel("previous"));
        }

        if (currentIndex < allPersons.size() - 1) {
            UUID nextId = allPersons.get(currentIndex + 1).getId();
            entityModel.add(linkTo(methodOn(PersonController.class).getPersonById(nextId)).withRel("next"));
        }

        return entityModel;
    }

    @Override
    public EntityModel<PersonViewDto> createPerson(PersonViewDto person) {
        com.web.serviceorientedweb.services.dtos.PersonViewDto servicePersonViewDto = modelMapper.map(person, com.web.serviceorientedweb.services.dtos.PersonViewDto.class);
        com.web.serviceorientedweb.services.dtos.PersonViewDto createdServicePerson = personService.createPerson(servicePersonViewDto);
        Message message = new Message(("Person created: " + createdServicePerson.getId()).getBytes());
        rabbitTemplate.send(exchange, routingKey, message);
        PersonViewDto createdPerson = modelMapper.map(createdServicePerson, PersonViewDto.class);
        return EntityModel.of(createdPerson,
                linkTo(methodOn(PersonController.class).getPersonById(createdServicePerson.getId())).withSelfRel(),
                linkTo(methodOn(PersonController.class).getAllPersons()).withRel("all-persons"));
    }

    @Override
    public void deletePerson(UUID id) {
        personService.deletePerson(id);
        rabbitTemplate.convertAndSend(exchange, routingKey, "Person deleted: " + id);
    }

    @Override
    public ResponseEntity<Void> changePersonRace(UUID id, String raceName) {
        boolean updated = personService.changeRace(id, raceName);
        if (!updated) {
            Message message = new Message(("Race " + raceName + " is full. Cannot add person " + id).getBytes());
            rabbitTemplate.send(exchange, routingKey, message);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Message message = new Message(("Race changed successfully for person " + id + " to " + raceName).getBytes());
        rabbitTemplate.send(exchange, routingKey, message);
        return ResponseEntity.ok().build();
    }
}
