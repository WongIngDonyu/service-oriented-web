package com.web.serviceorientedweb.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.serviceorientedweb.models.Transport;

import java.util.UUID;

public class TransportViewDto {
    @JsonIgnore
    private UUID id;
    private Transport.Type type;
    private String model;
    private int capacity;
    public TransportViewDto() {}

    public TransportViewDto(Transport.Type type, String model, int capacity) {
        this.type = type;
        this.model = model;
        this.capacity = capacity;
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

    public UUID getId() {return id;}

}
