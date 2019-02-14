package com.steereengineering.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import com.steereengineering.configuration.EmailDetails;

import com.steereengineering.services.EmailService;

@MessageEndpoint
public class NewEmailProcessor {

    private EmailService emailService;

    @Autowired
    public NewEmailProcessor(EmailService emailService){
        this.emailService = emailService;
    }

    @ServiceActivator(inputChannel = "emailInput")
    public void onNewEmailRequest(Message<EmailDetails> emailDetails) throws Exception{
        emailService.processEmailRequest(emailDetails.getPayload());
    }
}