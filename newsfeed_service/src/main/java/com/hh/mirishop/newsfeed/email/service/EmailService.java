package com.hh.newsfeed.email.service;

import com.hh.newsfeed.email.repository.EmailRequest;

public interface EmailService {

    void authEmail(EmailRequest request);

    boolean verityEmail(String email, String verificationCode);
}
