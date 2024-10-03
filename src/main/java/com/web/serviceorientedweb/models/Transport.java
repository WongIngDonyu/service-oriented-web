package com.web.serviceorientedweb.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "transports")
public class Transport extends BaseEnity{
    private Type type;
    private String model;
    private int capacity;
    private List<Race> races;
    public Transport() {}

    public Transport(Type type, String model, int capacity, List<Race> races) {
        this.type = type;
        this.model = model;
        this.capacity = capacity;
        this.races = races;
    }

    @Column(name = "type")
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Column(name = "model", unique = true)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "capacity")
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @OneToMany(mappedBy = "transport", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    public List<Race> getRaces() {
        return races;
    }

    public void setRaces(List<Race> races) {
        this.races = races;
    }

    public enum Type {
        Bus(0), Train(1), Plane(2), Ship(3);

        int number;
        Type(int number) {
            this.number=number;
        }
        public int getNumber(){
            return number;
        }
    }
}
