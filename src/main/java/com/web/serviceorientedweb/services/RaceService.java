package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.models.Race;

import java.util.List;
import java.util.UUID;

public interface RaceService <I extends UUID>{
    List<Race> getAllRaces();
    Race getRaceById(UUID id);
    Race createRace(Race race);
    void deleteRace(UUID id);
}
