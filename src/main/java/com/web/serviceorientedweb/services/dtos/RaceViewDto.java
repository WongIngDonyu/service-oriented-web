package com.web.serviceorientedweb.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.UUID;

public class RaceViewDto {
    @JsonIgnore
    private UUID id;
    private String raceName;
    private String departure;
    private String destination;
    private Date raceDate;
    private int price;
    private String model;
    public RaceViewDto() {}

    public RaceViewDto(String raceName, String departure, String destination, Date raceDate, int price,String model) {
        this.raceName = raceName;
        this.departure = departure;
        this.destination = destination;
        this.raceDate = raceDate;
        this.price = price;
        this.model = model;
    }

    public String getRaceName() {
        return raceName;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public Date getRaceDate() {
        return raceDate;
    }

    public int getPrice() {
        return price;
    }


    public String getModel() {
        return model;
    }
    public UUID getId() {return id;}

    public void setModel(String model) {
        this.model = model;
    }

}
