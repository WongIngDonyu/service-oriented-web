package com.web.serviceorientedweb.services.impl;

import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.repositories.RaceRepository;
import com.web.serviceorientedweb.services.RaceService;
import com.web.serviceorientedweb.services.dtos.RaceViewDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RaceServiceImpl implements RaceService<UUID> {
    private final RaceRepository raceRepository;
    private final ModelMapper modelMapper;
    private final TransportServiceImpl transportService;
    public RaceServiceImpl(RaceRepository raceRepository, ModelMapper modelMapper, TransportServiceImpl transportService)
    {this.raceRepository = raceRepository; this.modelMapper = modelMapper; this.transportService = transportService;}

    @Override
    public List<Race> getAllRaces() {
        return raceRepository.findAll();
    }

    @Override
    public Race getRaceById(UUID id) {
        return raceRepository.findById(id).orElse(null);
    }

    @Override
    public RaceViewDto createRace(RaceViewDto race) {
        Race newRace = modelMapper.map(race, Race.class);
        newRace.setTransport(transportService.getTransportByModel(race.getModel()));
        newRace = raceRepository.save(newRace);
        return modelMapper.map(newRace, RaceViewDto.class);
    }


    @Override
    public void deleteRace(UUID id) {
        raceRepository.deleteById(id);
    }

    @Override
    public Race findRaceByName(String name) {
        return raceRepository.findByRaceName(name).orElse(null);
    }

    @Override
    public List<Race> findRacesByNames(List<String> names) {
        List<Race> foundRaces = new ArrayList<>();
        for (String name : names) {
            Race race = raceRepository.findByRaceName(name).orElse(null);
            if (race != null) {
                foundRaces.add(race);
            }
        }
        return foundRaces;
    }
}
