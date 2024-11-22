package com.web.serviceorientedweb.web;

import com.web.serviceorientedweb.services.RaceService;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.web.transportapi.controllers.RaceApi;
import org.web.transportapi.dto.RaceDto;
import org.web.transportapi.dto.RaceViewDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RaceController implements RaceApi {

    private final RaceService raceService;
    private final RabbitTemplate rabbitTemplate;
    private final ModelMapper modelMapper;
    private final String exchange = "races-exchange";
    private final String routingKey = "races-routing-key";

    public RaceController(RaceService raceService, RabbitTemplate rabbitTemplate, ModelMapper modelMapper) {
        this.raceService = raceService;
        this.rabbitTemplate = rabbitTemplate;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<EntityModel<RaceDto>> getAllRaces() {
        List<com.web.serviceorientedweb.services.dtos.RaceDto> serviceRaceDtos = raceService.getAllRaces();
        List<EntityModel<RaceDto>> raceModels = new ArrayList<>();
        for (com.web.serviceorientedweb.services.dtos.RaceDto serviceDto : serviceRaceDtos) {
            RaceDto raceDto = modelMapper.map(serviceDto, RaceDto.class);
            EntityModel<RaceDto> entityModel = EntityModel.of(raceDto,
                    linkTo(methodOn(RaceController.class).getRaceById(serviceDto.getId())).withSelfRel(),
                    linkTo(methodOn(RaceController.class).getAllRaces()).withRel("all-races"));
            raceModels.add(entityModel);
        }

        return raceModels;
    }

    @Override
    public EntityModel<RaceViewDto> getRaceById(UUID id) {
        com.web.serviceorientedweb.services.dtos.RaceViewDto serviceRaceViewDto = raceService.getRaceById(id);
        RaceViewDto raceViewDto = modelMapper.map(serviceRaceViewDto, RaceViewDto.class);
        List<com.web.serviceorientedweb.services.dtos.RaceDto> allRaces = raceService.getAllRaces();
        int currentIndex = -1;
        for (int i = 0; i < allRaces.size(); i++) {
            if (allRaces.get(i).getId().equals(id)) {
                currentIndex = i;
                break;
            }
        }
        EntityModel<RaceViewDto> entityModel = EntityModel.of(raceViewDto,
                linkTo(methodOn(RaceController.class).getRaceById(id)).withSelfRel(),
                linkTo(methodOn(RaceController.class).getAllRaces()).withRel("all-races"));
        if (currentIndex > 0) {
            UUID previousId = allRaces.get(currentIndex - 1).getId();
            entityModel.add(linkTo(methodOn(RaceController.class).getRaceById(previousId)).withRel("previous"));
        }
        if (currentIndex < allRaces.size() - 1) {
            UUID nextId = allRaces.get(currentIndex + 1).getId();
            entityModel.add(linkTo(methodOn(RaceController.class).getRaceById(nextId)).withRel("next"));
        }

        return entityModel;
    }

    @Override
    public EntityModel<RaceViewDto> createRace(RaceViewDto race) {
        com.web.serviceorientedweb.services.dtos.RaceViewDto serviceRaceViewDto = modelMapper.map(race, com.web.serviceorientedweb.services.dtos.RaceViewDto.class);
        com.web.serviceorientedweb.services.dtos.RaceViewDto createdServiceRace = raceService.createRace(serviceRaceViewDto);
        rabbitTemplate.convertAndSend(exchange, routingKey, "Race created: " + createdServiceRace.getId());
        RaceViewDto createdRace = modelMapper.map(createdServiceRace, RaceViewDto.class);
        return EntityModel.of(createdRace,
                linkTo(methodOn(RaceController.class).getRaceById(createdServiceRace.getId())).withSelfRel(),
                linkTo(methodOn(RaceController.class).getAllRaces()).withRel("all-races"));
    }

    @Override
    public void deleteRace(UUID id) {
        raceService.deleteRace(id);
        rabbitTemplate.convertAndSend(exchange, routingKey, "Race deleted: " + id);
    }

    @Override
    public ResponseEntity<String> updateRaceTime(UUID id, String newTime) {
        raceService.updateRaceTime(id, LocalDateTime.parse(newTime));
        rabbitTemplate.convertAndSend(exchange, routingKey, "Race time updated: " + id + ", New time: " + newTime);
        return ResponseEntity.ok("Race time updated");
    }
}
