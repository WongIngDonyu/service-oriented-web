package com.web.serviceorientedweb.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "races")
public class Race extends BaseEnity {
    private String raceName;
    private String departure;
    private String destination;
    private LocalDateTime raceDate;
    private int price;
    private List<Person> persons;
    private Transport transport;

    public Race() {}

    public Race(String raceName, String departure, String destination, LocalDateTime raceDate, int price, List<Person> persons, Transport transport) {
        this.raceName = raceName;
        this.departure = departure;
        this.destination = destination;
        this.raceDate = raceDate;
        this.price = price;
        this.persons = persons;
        this.transport = transport;
    }

    @Column(name = "raceName", unique = true)
    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    @Column(name = "departure")
    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    @Column(name = "destination")
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Column(name = "raceDate")
    public LocalDateTime getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(LocalDateTime raceDate) {
        this.raceDate = raceDate;
    }

    @Column(name = "price")
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @OneToMany(mappedBy = "race", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @ManyToOne
    @JoinColumn(name = "transport_id")
    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}