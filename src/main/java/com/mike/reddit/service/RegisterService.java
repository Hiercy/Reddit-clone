package com.mike.reddit.service;

import com.mike.reddit.dto.RegisterRequest;
import com.mike.reddit.model.Customer;
import com.mike.reddit.model.VerificationToken;
import com.mike.reddit.repository.CustomerRepository;
import com.mike.reddit.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.UUID;

@Service
public class RegisterService implements Register {

    private final CustomerRepository customerRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Autowired
    public RegisterService(CustomerRepository customerRepository, VerificationTokenRepository verificationTokenRepository, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.customerRepository = customerRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) {
        Customer customer = new Customer.Builder()
                .setUsername(registerRequest.getUsername())
                .setPassword(passwordEncoder.encode(registerRequest.getPassword()))
                .setEmail(registerRequest.getEmail())
                .setCreatedDate(Instant.now())
                .setEnabled(true)
                .build();

        customerRepository.save(customer);

        String token = generateToken(customer);
        sendVerificationMail(customer, token);
    }

    private String generateToken(Customer customer) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setCustomer(customer);
        verificationToken.setCreatedDate(Instant.now());

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    private void sendVerificationMail(Customer customer, String token) {
        if (!StringUtils.isEmpty(customer.getEmail())) {
            String message = String.format("Hello, %s! Welcome to Reddit-clone. Please visit next link: http://localhost:8080/activate/%s",
                    customer.getUsername(),
                    token);

            mailSender.send(customer.getEmail(), "Activation code from Reddit-clone", message);
        }
    }
}
