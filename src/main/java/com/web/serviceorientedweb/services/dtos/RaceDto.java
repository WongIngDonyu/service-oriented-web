package com.web.serviceorientedweb.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.UUID;

public class RaceDto {
    @JsonIgnore
    private UUID id;
    private String raceName;
    private Date raceDate;

    public RaceDto(){}
    public RaceDto(Date raceDate, String raceName) {
        this.raceDate = raceDate;
        this.raceName = raceName;
    }

    public String getRaceName() {
        return raceName;
    }

    public Date getRaceDate() {
        return raceDate;
    }

    public UUID getId() {
        return id;
    }
}
