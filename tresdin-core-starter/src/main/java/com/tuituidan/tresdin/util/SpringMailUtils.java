package com.tuituidan.tresdin.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * SpringMailUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026/1/31
 */
@Component
@Slf4j
public class SpringMailUtils implements ApplicationContextAware {

    private static MailProperties mailProperties;

    private static JavaMailSender javaMailSender;

    /**
     * 发送简单文本邮件
     *
     * @param subject subject
     * @param content content
     * @param receiver receiver
     */
    public static void sendText(String subject, String content, String... receiver) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(content);
        message.setTo(receiver);
        message.setFrom(mailProperties.getUsername());
        javaMailSender.send(message);
    }

    /**
     * 发送html邮件
     *
     * @param subject subject
     * @param content content
     * @param receiver receiver
     */
    public static void sendHtml(String subject, String content, String... receiver) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setTo(receiver);
            helper.setFrom(mailProperties.getUsername());
            javaMailSender.send(message);
        } catch (MessagingException ex) {
            log.error("发送邮件失败", ex);
            throw new IllegalArgumentException("发送邮件失败", ex);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        initSender(applicationContext);
    }

    private static void initSender(ApplicationContext applicationContext) {
        mailProperties = applicationContext.getBean(MailProperties.class);
        javaMailSender = applicationContext.getBean(JavaMailSender.class);
    }

}
