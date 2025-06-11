package br.com.dbc.vemser.lojafashionapi.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final Configuration fmConfiguration;
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public void sendEmail(String subject, String templateName, Map<String, Object> model, String recipientEmail) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(mailFrom);
            mimeMessageHelper.setTo("hugosantos.ufg@gmail.com");
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(getContentFromTemplate(templateName, model), true);

            log.info("Enviando e-mail: Assunto='{}', Para='{}', Template='{}'", subject, recipientEmail, templateName);
            emailSender.send(mimeMessageHelper.getMimeMessage());
            log.info("E-mail enviado com sucesso para: {}", recipientEmail);

        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Erro ao enviar e-mail para {}: {}", recipientEmail, e.getMessage());
            e.printStackTrace();
        }
    }

    private String getContentFromTemplate(String templateName, Map<String, Object> model) throws IOException, TemplateException {
        Map<String, Object> mutableModel = new HashMap<>(model);
        mutableModel.put("emailSuporte", mailFrom);

        Template template = fmConfiguration.getTemplate(templateName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, mutableModel);
    }
}