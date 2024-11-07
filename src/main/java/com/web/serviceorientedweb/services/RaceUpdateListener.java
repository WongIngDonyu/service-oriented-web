package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.models.Person;
import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.repositories.RaceRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RaceUpdateListener {

    private final NotificationService notificationService;
    private final RaceRepository raceRepository;

    @Autowired
    public RaceUpdateListener(NotificationService notificationService, RaceRepository raceRepository) {
        this.notificationService = notificationService;
        this.raceRepository = raceRepository;
    }

    @RabbitListener(queues = "races-queue")
    public void onRaceUpdate(String message) {
        String[] messageParts = message.split(", New time: ");
        UUID raceId = UUID.fromString(messageParts[0].replace("Race time updated: ", "").trim());
        String newTime = messageParts[1].trim();
        Race race = raceRepository.findWithPersonsById(raceId).orElseThrow(() -> new IllegalArgumentException("Race not found with ID: " + raceId));
        for (Person person : race.getPersons()) {
            notificationService.sendNotification(person, "Race " + race.getRaceName() + " time updated to " + newTime);
        }
    }
}
