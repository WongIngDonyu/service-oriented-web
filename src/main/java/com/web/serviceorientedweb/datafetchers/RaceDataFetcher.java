package com.web.serviceorientedweb.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.web.serviceorientedweb.services.RaceService;
import com.web.serviceorientedweb.services.dtos.RaceDto;
import com.web.serviceorientedweb.services.dtos.RaceViewDto;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        return raceService.getAllRaces();
    }

    @DgsQuery
    public RaceViewDto raceById(DataFetchingEnvironment dataFetchingEnvironment) {
        UUID id = UUID.fromString(dataFetchingEnvironment.getArgument("id"));
        RaceViewDto raceViewDto = raceService.getRaceById(id);
        return raceViewDto;
    }

    @DgsMutation
    public RaceDto createRace(DataFetchingEnvironment dataFetchingEnvironment) throws ParseException {
        Map<String, Object> raceInput = dataFetchingEnvironment.getArgument("race");
        Date raceDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse((String) raceInput.get("raceDate"));
        RaceViewDto createdRace = raceService.createRace(new RaceViewDto(
                (String) raceInput.get("raceName"),
                (String) raceInput.get("departure"),
                (String) raceInput.get("destination"),
                raceDate,
                (Integer) raceInput.get("price"),
                (String) raceInput.get("model")
        ));
        return new RaceDto(createdRace.getRaceDate(), createdRace.getRaceName());
    }

    @DgsMutation
    public String deleteRace(DataFetchingEnvironment dataFetchingEnvironment) {
        UUID id = UUID.fromString(dataFetchingEnvironment.getArgument("id"));
        raceService.deleteRace(id);
        return "Race with ID " + id + " was deleted";
    }
}


