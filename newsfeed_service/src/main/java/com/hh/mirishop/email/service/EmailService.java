package com.hh.mirishop.email.service;

import com.hh.mirishop.email.repository.EmailRequest;

public interface EmailService {

    void authEmail(EmailRequest request);

    boolean verityEmail(String email, String verificationCode);
}
