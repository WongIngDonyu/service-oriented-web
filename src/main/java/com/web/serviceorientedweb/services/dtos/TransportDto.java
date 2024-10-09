package com.web.serviceorientedweb.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.serviceorientedweb.models.Transport;

import java.util.UUID;

public class TransportDto {
    @JsonIgnore
    private UUID id;
    private Transport.Type type;
    private String model;
    public TransportDto(){}

    public TransportDto(Transport.Type type, String model) {
        this.type = type;
        this.model = model;
    }

    public Transport.Type getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public UUID getId() {
        return id;
    }
}
