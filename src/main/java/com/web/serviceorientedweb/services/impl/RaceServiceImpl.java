package com.web.serviceorientedweb.services.impl;

import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.repositories.RaceRepository;
import com.web.serviceorientedweb.services.RaceService;
import com.web.serviceorientedweb.services.dtos.RaceDto;
import com.web.serviceorientedweb.services.dtos.RaceViewDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RaceServiceImpl implements RaceService<UUID> {
    private final RaceRepository raceRepository;
    private final ModelMapper modelMapper;
    private final TransportServiceImpl transportService;

    public RaceServiceImpl(RaceRepository raceRepository, ModelMapper modelMapper, TransportServiceImpl transportService) {
        this.raceRepository = raceRepository;
        this.modelMapper = modelMapper;
        this.transportService = transportService;
    }

    @Override
    public List<RaceDto> getAllRaces() {
        return raceRepository.findAll().stream().map(race -> modelMapper.map(race, RaceDto.class)).collect(Collectors.toList());
    }

    @Override
    public RaceViewDto getRaceById(UUID id) {
        return modelMapper.map(raceRepository.findById(id).orElse(null), RaceViewDto.class);
    }

    @Override
    public RaceViewDto createRace(RaceViewDto race) {
        Race newRace = modelMapper.map(race, Race.class);
        newRace.setTransport(transportService.getTransportByModel(race.getModel()));
        return modelMapper.map(raceRepository.saveAndFlush(newRace), RaceViewDto.class);
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
    public String getTransportModelByRaceId(UUID id) {
        return raceRepository.findById(id).orElse(null).getTransport().getModel();
    }

    @Override
    public Integer getAvaibleSeatsByRaceName(String raceName) {
        Race race = raceRepository.findByRaceName(raceName).orElse(null);
        int capacity = race.getTransport().getCapacity();
        int bookedSeats = race.getPersons().size();
        return bookedSeats - capacity;
    }

}
