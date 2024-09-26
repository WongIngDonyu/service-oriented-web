package com.web.serviceorientedweb.services.dtos;

import java.util.Date;

public class RaceDto {
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
}
