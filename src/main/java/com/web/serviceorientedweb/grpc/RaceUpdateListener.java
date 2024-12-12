package com.web.serviceorientedweb.grpc;

import com.web.serviceorientedweb.models.Person;
import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.repositories.RaceRepository;
import com.web.serviceorientedweb.services.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            UUID raceId = UUID.fromString(messageParts[0].replace("Race update request: ", "").trim());
            String newTimeStr = messageParts[1].trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime newDateTime = LocalDateTime.parse(newTimeStr, formatter);
            long newTime = newDateTime.toEpochSecond(ZoneOffset.UTC);
            long currentTime = Instant.now().getEpochSecond();
            boolean isValid = raceValidationService.validateRaceTime(currentTime, newTime);
            if (isValid) {
                Race race = raceRepository.findWithPersonsById(raceId).orElseThrow(() -> new IllegalArgumentException("Race not found with ID: " + raceId));
                race.setRaceDate(newDateTime);
                raceRepository.save(race);
                for (Person person : race.getPersons()) {
                    notificationService.sendNotification(person, "Race " + race.getRaceName() + " time updated to " + newTimeStr);
                }
                notificationService.sendBroadcastNotification("Race " + race.getRaceName() + " time updated to " + newTimeStr);
                System.out.println("Race time updated successfully for race ID: " + raceId);
            } else {
                System.out.println("Invalid race time update for race ID: " + raceId + ". New time must be within 12 hours and greater than the current time.");
            }
        } catch (Exception e) {
            System.err.println("Failed to process message: " + message);
            e.printStackTrace();
        }
    }
}