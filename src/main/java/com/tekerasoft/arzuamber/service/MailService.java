package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.OrderDto;
import com.tekerasoft.arzuamber.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class MailService {

    @Value("${spring.order-mail}")
    private String from;

    @Value("${spring.origin.url}")
    private String originUrl;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserService userService;

    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine, UserService userService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.userService = userService;
    }

    public void sendOrderConfirmationMail(OrderDto orderDto) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);

            Context context = new Context();
            // Buyer
            context.setVariable("name", orderDto.getBuyer().getName());
            context.setVariable("surname", orderDto.getBuyer().getSurname());
            context.setVariable("email", orderDto.getBuyer().getEmail());
            context.setVariable("phoneNumber", orderDto.getBuyer().getGsmNumber());
            context.setVariable("createdAt", orderDto.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            // Address order number
            context.setVariable("shippingAddress", orderDto.getShippingAddress().getAddress());
            context.setVariable("orderNumber", orderDto.getPaymentId());
            //Basket Item
            context.setVariable("basketItems", orderDto.getBasketItems());

            context.setVariable("totalPrice", orderDto.getTotalPrice());

            String htmlContent = templateEngine.process("order-mail", context);

            helper.setTo(orderDto.getBuyer().getEmail());
            helper.setSubject("Sipariş Detaylarınız");
            helper.setText(htmlContent, true);
            helper.setFrom(new InternetAddress(from, "Arzuamber Moda"));

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendForgotPasswordMail(String email) throws MessagingException {
        try {
            Optional<User> user = userService.getByUsername(email);
            if (user.isPresent()) {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);

                String createdToken = userService.createToken(email);

                Context context = new Context();
                context.setVariable("callbackUrl", originUrl+"/tr/forgot-password?"+"mail="+email+"&_cpjwt="+createdToken);

                String htmlContent = templateEngine.process("change-password-mail", context);

                helper.setTo(email);
                helper.setSubject("Parola Değişikliği");
                helper.setText(htmlContent, true);
                helper.setFrom(new InternetAddress(from, "Arzuamber Moda"));
                mailSender.send(message);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

}
