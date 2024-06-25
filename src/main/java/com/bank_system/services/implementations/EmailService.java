package com.bank_system.services.implementations;

import com.bank_system.models.Customer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final ITemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, ITemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    //sendTransactionEmail
    public void sendTransferEmail(Customer fromCustomer, Customer toCustomer, double amount) throws MessagingException {
        // Prepare the email template
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        // Create Thymeleaf context and set variables
        Context context = new Context();
        context.setVariable("fromCustomer", fromCustomer.getFirstName() + " " + fromCustomer.getLastName());
        context.setVariable("toCustomer", toCustomer.getFirstName() + " " + toCustomer.getLastName());
        context.setVariable("amount", amount);

        // Process the email template with Thymeleaf
        String htmlContentSend = templateEngine.process("transfer-send-email.html", context);

        // Set up MimeMessageHelper with sender, subject, and HTML content
        helper.setTo(fromCustomer.getEmail()); // Send to sender
        helper.setSubject("Transaction: Transfer - Sending");
        helper.setText(htmlContentSend, true);

        // Send email to sender
        javaMailSender.send(mimeMessage);

        // Prepare another MimeMessage for the recipient
        MimeMessage recipientMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper recipientHelper = new MimeMessageHelper(recipientMessage, true);

        String htmlContentReceive = templateEngine.process("transfer-receive-email.html", context);

        // Set up MimeMessageHelper for recipient
        recipientHelper.setTo(fromCustomer.getEmail()); // Send to recipient
        recipientHelper.setSubject("Transaction: Transfer - Receiving");
        recipientHelper.setText(htmlContentReceive, true);

        // Send email to recipient
        javaMailSender.send(recipientMessage);
    }

    public void sendSavingEmail(Customer customer, double amount) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Context context = new Context();
        context.setVariable("customer", customer.getFirstName() + " " + customer.getLastName());
        context.setVariable("amount", amount);

        String htmlContent = templateEngine.process("saving-email.html", context);

        helper.setTo(customer.getEmail());
        helper.setSubject("Transaction: Saving");
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendWithdrawEmail(Customer customer, double amount) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Context context = new Context();
        context.setVariable("customer", customer.getFirstName() + " " + customer.getLastName());
        context.setVariable("amount", amount);

        String htmlContent = templateEngine.process("withdraw-email.html", context);

        helper.setTo(customer.getEmail());
        helper.setSubject("Transaction: Withdrawing");
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }
}
