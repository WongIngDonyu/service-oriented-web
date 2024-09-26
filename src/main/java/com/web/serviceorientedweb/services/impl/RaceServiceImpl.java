package com.web.serviceorientedweb.services.impl;

import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.repositories.RaceRepository;
import com.web.serviceorientedweb.services.RaceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RaceServiceImpl implements RaceService<UUID> {
    private final RaceRepository raceRepository;
    public RaceServiceImpl(RaceRepository raceRepository) {this.raceRepository = raceRepository;}

    @Override
    public List<Race> getAllRaces() {
        return raceRepository.findAll();
    }

    @Override
    public Race getRaceById(UUID id) {
        return raceRepository.findById(id).orElse(null);
    }

    @Override
    public Race createRace(Race race) {
        return raceRepository.save(race);
    }

    @Override
    public void deleteRace(UUID id) {
        raceRepository.deleteById(id);
    }
}
