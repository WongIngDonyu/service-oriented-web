package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(Person person, String message) {
        System.out.println("Notification sent to " + person.getFirstName() + ": " + message);
    }

    public void sendNotificationWebSocket(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}

