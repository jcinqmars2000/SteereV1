package com.steereengineering.services;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.steereengineering.configuration.EmailDetails;

@Service
public class EmailService {

    private JavaMailSender sender;
    

    @Autowired
    public EmailService(JavaMailSender sender){
        this.sender = sender;
    }

    public void processEmailRequest(EmailDetails emailDetails) throws Exception{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(emailDetails.getEmailTo());
        helper.setText(emailDetails.getEmailBody());
        helper.setSubject(emailDetails.getEmailSubject());

        if(emailDetails.getEmailCC()!= null){
            helper.setCc(emailDetails.getEmailCC());
        }

        sender.send(message);
    }

}