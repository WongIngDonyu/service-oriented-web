package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.services.dtos.RaceViewDto;

import java.util.List;
import java.util.UUID;

public interface RaceService <I extends UUID>{
    List<Race> getAllRaces();
    Race getRaceById(UUID id);
    RaceViewDto createRace(RaceViewDto race);
    void deleteRace(UUID id);
    Race findRaceByName(String name);
    List<Race> findRacesByNames(List<String> names);
}
