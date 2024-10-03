package com.web.serviceorientedweb.services.dtos;

import com.web.serviceorientedweb.models.Transport;

import java.util.UUID;

public class TransportDto {
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
}
