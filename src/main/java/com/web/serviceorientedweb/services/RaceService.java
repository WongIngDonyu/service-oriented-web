package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.services.dtos.RaceDto;
import com.web.serviceorientedweb.services.dtos.RaceViewDto;

import java.util.List;
import java.util.UUID;

public interface RaceService <I extends UUID>{
    List<RaceDto> getAllRaces();
    RaceViewDto getRaceById(UUID id);
    RaceViewDto createRace(RaceViewDto race);
    void deleteRace(UUID id);
    Race findRaceByName(String name);
    String getTransportModelByRaceId(UUID id);
    Integer getAvaibleSeatsByRaceName(String raceName);
}
