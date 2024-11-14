package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.models.Person;
import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.repositories.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class RaceUpdateListener {

    private final NotificationService notificationService;
    private final RaceRepository raceRepository;
    private final RaceValidationService raceValidationService;

    @Autowired
    public RaceUpdateListener(NotificationService notificationService, RaceRepository raceRepository, RaceValidationService raceValidationService) {
        this.notificationService = notificationService;
        this.raceRepository = raceRepository;
        this.raceValidationService = raceValidationService;
    }

    @RabbitListener(queues = "races-queue")
    public void onRaceUpdate(String message) {
        try {
            String[] messageParts = message.split(", New time: ");
            UUID raceId = UUID.fromString(messageParts[0].replace("Race time updated: ", "").trim());
            String newTimeStr = messageParts[1].trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime newDateTime = LocalDateTime.parse(newTimeStr, formatter);
            long newTime = newDateTime.toEpochSecond(ZoneOffset.UTC);
            long currentTime = Instant.now().getEpochSecond();
            boolean isValid = raceValidationService.validateRaceTime(currentTime, newTime);
            if (isValid) {
                Race race = raceRepository.findWithPersonsById(raceId).orElseThrow(() -> new IllegalArgumentException("Race not found with ID: " + raceId));
                for (Person person : race.getPersons()) {
                    notificationService.sendNotification(person, "Race " + race.getRaceName() + " time updated to " + newTimeStr);
                }
            } else {
                System.out.println("Invalid race time update for race ID: " + raceId);
            }
        } catch (Exception e) {
            System.err.println("Failed to process message: " + message);
            e.printStackTrace();
        }
    }
}
