package com.web.serviceorientedweb.models;

import jakarta.persistence.*;

import java.util.UUID;

@MappedSuperclass
public abstract class BaseEnity {
    protected UUID id;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = true)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
