package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.entity.VerificationEntity;
import com.daeseonbae.DSBBackend.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class EmailService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    private String generateRandomCode() { //인증번호 생성
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public void sendSimpleMessage(String to, String subject, String text) { //메일 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendVerificationCode(String to) { //인증 코드 전송
        String code = generateRandomCode();
        sendSimpleMessage(to, "Your Verification Code", "Your verification code is: " + code);

//      생성된 인증 코드 저장
        VerificationEntity verificationCode = new VerificationEntity();
        verificationCode.setEmail(to);
        verificationCode.setCode(code);
        verificationCodeRepository.save(verificationCode);
    }
    @Transactional
    public boolean verifyCode(String email, String code) { //인증 코드 검증 및 성공 시 삭제
        boolean isValid = verificationCodeRepository.findByEmailAndCode(email, code).isPresent();
        if (isValid) {
            verificationCodeRepository.deleteByEmailAndCode(email, code);
        }
        return isValid;
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
//  주기적으로 만료된 코드 삭제
    public void deleteExpiredCodes() {
//      3분 이전 생성된 코드 삭제
        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(3);
        verificationCodeRepository.deleteByCreatedAtBefore(expiryTime);
    }
}
