package com.web.serviceorientedweb.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.models.Transport;

import java.util.List;
import java.util.UUID;

public class TransportViewDto {
    @JsonIgnore
    private UUID id;
    private Transport.Type type;
    private String model;
    private int capacity;
    private List<String> racesName;
    public TransportViewDto() {}

    public TransportViewDto(Transport.Type type, String model, int capacity, List<String> racesName) {
        this.type = type;
        this.model = model;
        this.capacity = capacity;
        this.racesName = racesName;
    }

    public Transport.Type getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<String> getRacesName() {
        return racesName;
    }
    public UUID getId() {return id;}

    public void setRacesName(List<String> racesName) {
        this.racesName = racesName;
    }

}
