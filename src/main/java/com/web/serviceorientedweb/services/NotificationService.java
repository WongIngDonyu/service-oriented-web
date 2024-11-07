package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.models.Person;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(Person person, String message) {
        System.out.println("Notification sent to " + person.getFirstName() + ": " + message);
    }
}
