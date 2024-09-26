package com.web.serviceorientedweb.web;

import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.services.RaceService;
import com.web.serviceorientedweb.services.dtos.RaceDto;
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
@RequestMapping("/api/races")
public class RaceController {

    private RaceService raceService;

    @Autowired
    public void setRaceService(RaceService raceService) {this.raceService = raceService;}

    @GetMapping("/all")
    public List<EntityModel<RaceDto>> getAllRaces() {
        List<Race> races = raceService.getAllRaces();
        List<EntityModel<RaceDto>> raceDtos = new ArrayList<>();
        for (Race race : races) {
            RaceDto raceDto = new RaceDto(race.getRaceDate(), race.getRaceName());
            EntityModel<RaceDto> resource = EntityModel.of(raceDto,
                    linkTo(methodOn(RaceController.class).getRaceById(race.getId())).withSelfRel());
            raceDtos.add(resource);
        }
        return raceDtos;
    }

    @GetMapping("/{id}")
    public EntityModel<RaceDto> getRaceById(@PathVariable UUID id) {
        Race race = raceService.getRaceById(id);
        RaceDto raceDto = new RaceDto(race.getRaceDate(), race.getRaceName());
        EntityModel<RaceDto> resource = EntityModel.of(raceDto,
                linkTo(methodOn(RaceController.class).getRaceById(race.getId())).withSelfRel(),
                linkTo(methodOn(RaceController.class).getAllRaces()).withRel("all-races"));

        List<Race> races = raceService.getAllRaces();
        int raceIndex = races.indexOf(race);
        if (raceIndex < races.size() - 1) {
            resource.add(linkTo(methodOn(RaceController.class).getRaceById(races.get(raceIndex + 1).getId())).withRel("next"));
        }
        if (raceIndex > 0) {
            resource.add(linkTo(methodOn(RaceController.class).getRaceById(races.get(raceIndex - 1).getId())).withRel("previous"));
        }
        return resource;
    }

    @PostMapping
    public EntityModel<RaceDto> createRace(@RequestBody Race race) {
        Race createdRace = raceService.createRace(race);
        RaceDto raceDto = new RaceDto(race.getRaceDate(), race.getRaceName());
        EntityModel<RaceDto> resource = EntityModel.of(raceDto,
                linkTo(methodOn(RaceController.class).getRaceById(createdRace.getId())).withSelfRel(),
                linkTo(methodOn(RaceController.class).getAllRaces()).withRel("all-races"));
        return resource;
    }

    @DeleteMapping("/{id}")
    public EntityModel<String> deleteRace(@PathVariable UUID id) {
        raceService.deleteRace(id);
        EntityModel<String> resource = EntityModel.of("Race with ID " + id + " was deleted",
                linkTo(methodOn(RaceController.class).getAllRaces()).withRel("all-races"));
        return resource;
    }
}
