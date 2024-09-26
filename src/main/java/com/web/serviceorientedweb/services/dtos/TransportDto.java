package com.web.serviceorientedweb.services.dtos;

import com.web.serviceorientedweb.models.Transport;

import java.util.UUID;

public class TransportDto {
    private UUID id;
    private Transport.Type type;
    private String model;
    public TransportDto(){}

    public TransportDto(UUID id, Transport.Type type, String model) {
        this.id = id;
        this.type = type;
        this.model = model;
    }

    public UUID getId() {
        return id;
    }

    public Transport.Type getType() {
        return type;
    }

    public String getModel() {
        return model;
    }
}
