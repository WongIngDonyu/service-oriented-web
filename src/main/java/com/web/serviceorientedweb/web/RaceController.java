package com.web.serviceorientedweb.web;

import com.web.serviceorientedweb.services.RaceService;
import com.web.serviceorientedweb.services.dtos.RaceDto;
import com.web.serviceorientedweb.services.dtos.RaceViewDto;
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
@RequestMapping("/api/races")
public class RaceController {

    private final RaceService raceService;
    private final RabbitTemplate rabbitTemplate;
    private final String exchange = "races-exchange";
    private final String routingKey = "races-routing-key";

    @Autowired
    public RaceController(RaceService raceService, RabbitTemplate rabbitTemplate) {
        this.raceService = raceService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/all")
    public List<EntityModel<RaceDto>> getAllRaces() {
        List<RaceDto> raceDtos = raceService.getAllRaces();
        List<EntityModel<RaceDto>> entityModels = new ArrayList<>();
        for (RaceDto raceDto : raceDtos) {
            EntityModel<RaceDto> entityModel = EntityModel.of(raceDto,
                    linkTo(methodOn(RaceController.class).getRaceById(raceDto.getId())).withSelfRel());
            entityModels.add(entityModel);
        }
        return entityModels;
    }

    @GetMapping("/{id}")
    public EntityModel<RaceViewDto> getRaceById(@PathVariable UUID id) {
        RaceViewDto raceViewDto = raceService.getRaceById(id);
        String transportModel = raceService.getTransportModelByRaceId(id);
        raceViewDto.setModel(transportModel);
        EntityModel<RaceViewDto> entityModel = EntityModel.of(raceViewDto,
                linkTo(methodOn(RaceController.class).getRaceById(id)).withSelfRel(),
                linkTo(methodOn(RaceController.class).getAllRaces()).withRel("all-races"));
        List<RaceDto> races = raceService.getAllRaces();
        int raceIndex = -1;
        for (int i = 0; i < races.size(); i++) {
            if (races.get(i).getId().equals(id)) {
                raceIndex = i;
                break;
            }
        }
        if (raceIndex < races.size() - 1) {
            UUID nextRaceId = races.get(raceIndex + 1).getId();
            entityModel.add(linkTo(methodOn(RaceController.class).getRaceById(nextRaceId)).withRel("next"));
        }
        if (raceIndex > 0) {
            UUID previousRaceId = races.get(raceIndex - 1).getId();
            entityModel.add(linkTo(methodOn(RaceController.class).getRaceById(previousRaceId)).withRel("previous"));
        }
        return entityModel;
    }

    @PostMapping
    public EntityModel<RaceDto> createRace(@RequestBody RaceViewDto race) {
        RaceViewDto createdRace = raceService.createRace(race);
        RaceDto raceDto = new RaceDto(createdRace.getRaceDate(), createdRace.getRaceName());
        rabbitTemplate.convertAndSend(exchange, routingKey, "Race created: " + createdRace.getId());
        EntityModel<RaceDto> entityModel = EntityModel.of(raceDto,
                linkTo(methodOn(RaceController.class).getRaceById(createdRace.getId())).withSelfRel(),
                linkTo(methodOn(RaceController.class).getAllRaces()).withRel("all-races"));
        return entityModel;
    }

    @DeleteMapping("/{id}")
    public void deleteRace(@PathVariable UUID id) {
        raceService.deleteRace(id);
        rabbitTemplate.convertAndSend(exchange, routingKey, "Race deleted: " + id);
    }
}

