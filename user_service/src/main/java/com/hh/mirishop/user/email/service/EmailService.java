package com.hh.mirishop.user.email.service;

import com.hh.mirishop.user.email.repository.EmailRequest;

public interface EmailService {

    void authEmail(EmailRequest request);

    boolean verityEmail(String email, String verificationCode);
}
