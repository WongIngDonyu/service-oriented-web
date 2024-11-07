package com.web.serviceorientedweb.init;

import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.models.Person;
import com.web.serviceorientedweb.models.Transport;
import com.web.serviceorientedweb.repositories.PersonRepository;
import com.web.serviceorientedweb.repositories.RaceRepository;
import com.web.serviceorientedweb.repositories.TransportRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RaceRepository raceRepository;

    private final PersonRepository personRepository;

    private final TransportRepository transportRepository;

    public DataInitializer(RaceRepository raceRepository, PersonRepository personRepository, TransportRepository transportRepository) {
        this.raceRepository = raceRepository;
        this.personRepository = personRepository;
        this.transportRepository = transportRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
    }

    private void seedData() {
        Transport transport1 = new Transport(Transport.Type.Bus, "Mercedes Sprinter", 20, null);
        Transport transport2 = new Transport(Transport.Type.Train, "Volvo 9700", 40, null);
        Transport transport3 = new Transport(Transport.Type.Plane, "Boeing 228", 100, null);
        Transport transport4 = new Transport(Transport.Type.Ship, "Boeing 737", 180, null);
        Transport transport5 = new Transport(Transport.Type.Ship, "Airbus A320", 150, null);
        transportRepository.saveAll(Arrays.asList(transport1, transport2, transport3, transport4, transport5));
        List<Race> races = Arrays.asList(
                new Race("Mountain Adventure", "Denver", "Aspen", LocalDateTime.now(), 120, new ArrayList<>(), transport1),
                new Race("Beach Getaway", "Miami", "Key West", LocalDateTime.now(), 150, new ArrayList<>(), transport2),
                new Race("City Escape", "New York", "Philadelphia", LocalDateTime.now(), 200, new ArrayList<>(), transport3),
                new Race("Island Hop", "Honolulu", "Maui", LocalDateTime.now(), 350, new ArrayList<>(), transport4),
                new Race("Cultural Tour", "Berlin", "Prague", LocalDateTime.now(), 180, new ArrayList<>(), transport5),
                new Race("Nature Walk", "Seattle", "Olympic National Park", LocalDateTime.now(), 90, new ArrayList<>(), transport1),
                new Race("Desert Journey", "Phoenix", "Grand Canyon", LocalDateTime.now(), 220, new ArrayList<>(), transport2),
                new Race("European Express", "Paris", "Amsterdam", LocalDateTime.now(), 300, new ArrayList<>(), transport3),
                new Race("Safari Adventure", "Johannesburg", "Kruger National Park", LocalDateTime.now(), 400, new ArrayList<>(), transport4),
                new Race("Northern Lights Tour", "Reykjavik", "Akureyri", LocalDateTime.now(), 250, new ArrayList<>(), transport5)
        );
        raceRepository.saveAll(races);
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "Michael", "johndoe@example.com", "7(916)711-32-72", races.get(0)),
                new Person("Jane", "Smith", "Anne", "janesmith@example.com", "7(916)256-00-59", races.get(1)),
                new Person("James", "Brown", "Robert", "jamesbrown@example.com", "7(916)292-37-66", races.get(2)),
                new Person("Emily", "Davis", "Sue", "emilydavis@example.com", "7(916)421-33-04", races.get(3)),
                new Person("Michael", "Clark", "Peter", "michaelclark@example.com", "7(916)164-34-68", races.get(4)),
                new Person("Sarah", "Johnson", "Marie", "sarahjohnson@example.com", "7(916)256-20-50", races.get(5)),
                new Person("David", "Martinez", "Lucas", "davidmartinez@example.com", "7(916)526-29-25", races.get(6)),
                new Person("Sophia", "Lee", "Grace", "sophialee@example.com", "7(916)567-96-10", races.get(7)),
                new Person("Daniel", "Miller", "Isaac", "danielmiller@example.com", "7(916)742-11-07", races.get(8)),
                new Person("Emma", "Garcia", "Rose", "emmagarcia@example.com", "7(916)646-70-70", races.get(9))
        );
        personRepository.saveAll(persons);
        transport1.setRaces(Arrays.asList(races.get(0), races.get(5)));
        transport2.setRaces(Arrays.asList(races.get(1), races.get(6)));
        transport3.setRaces(Arrays.asList(races.get(2), races.get(7)));
        transport4.setRaces(Arrays.asList(races.get(3), races.get(8)));
        transport5.setRaces(Arrays.asList(races.get(4), races.get(9)));
        transportRepository.saveAll(Arrays.asList(transport1, transport2, transport3, transport4, transport5));
    }
}