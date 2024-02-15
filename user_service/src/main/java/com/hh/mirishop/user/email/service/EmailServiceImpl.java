package com.hh.mirishop.user.email.service;

import com.hh.mirishop.user.common.redis.service.CacheRedisService;
import com.hh.mirishop.user.email.repository.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final CacheRedisService cacheRedisService;

    @Override
    @Transactional
    public void authEmail(EmailRequest request) {
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111);

        sendAuthEmail(request.getEmail(), authKey);
    }

    @Override
    public boolean verityEmail(String email, String verificationCode) {
        String storedEmail = cacheRedisService.getData(verificationCode);
        return email.equals(storedEmail);
    }

    private void sendAuthEmail(String email, String authKey) {
        String subject = "메일 인증코드";
        String text = "회원 가입을 위한 인증번호는 " + authKey + "입니다. <br/>";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // 유효 시간(5분)동안 {email, authKey} 저장
        cacheRedisService.setDataExpire(authKey, email, 60 * 5L);
    }
}
