package com.web.serviceorientedweb.datafetchers;

import com.web.serviceorientedweb.models.Person;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.services.RaceService;
import com.web.serviceorientedweb.services.dtos.RaceDto;
import com.web.serviceorientedweb.services.dtos.RaceViewDto;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@DgsComponent
public class RaceDataFetcher {
    private RaceService raceService;

    @Autowired
    public void setRaceService(RaceService raceService) {
        this.raceService = raceService;
    }

    @DgsQuery
    public List<RaceDto> allRaces() {
        List<Race> races = raceService.getAllRaces();
        List<RaceDto> raceDtos = new ArrayList<>();
        for (Race race : races) {
            RaceDto raceDto = new RaceDto(race.getRaceDate(), race.getRaceName());
            raceDtos.add(raceDto);
        }
        return raceDtos;
    }

    @DgsQuery
    public RaceViewDto raceById(DataFetchingEnvironment dataFetchingEnvironment) {
        UUID id = UUID.fromString(dataFetchingEnvironment.getArgument("id"));
        Race race = raceService.getRaceById(id);
        List<String> phones = new ArrayList<>();
        for (Person person : race.getPersons()) {
            phones.add(person.getPhone());
        }

        return new RaceViewDto(
                race.getRaceName(),
                race.getDeparture(),
                race.getDestination(),
                race.getRaceDate(),
                race.getPrice(),
                phones,
                race.getTransport().getModel()
        );
    }

    @DgsMutation
    public RaceDto createRace(DataFetchingEnvironment dataFetchingEnvironment) {
        Map<String, Object> raceInput = dataFetchingEnvironment.getArgument("race");
        RaceViewDto newRace = raceService.createRace(new RaceViewDto(
                (String) raceInput.get("raceName"),
                (String) raceInput.get("departure"),
                (String) raceInput.get("destination"),
                (Date) raceInput.get("raceDate"),
                (Integer) raceInput.get("price"),
                (List<String>) raceInput.get("phones"),
                (String) raceInput.get("transportModel")
        ));
        return new RaceDto(newRace.getRaceDate(), newRace.getRaceName());
    }

    @DgsMutation
    public String deleteRace(DataFetchingEnvironment dataFetchingEnvironment) {
        UUID id = UUID.fromString(dataFetchingEnvironment.getArgument("id"));
        raceService.deleteRace(id);
        return "Race with ID " + id + " was deleted";
    }
}


