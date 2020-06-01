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

import java.time.Instant;
import java.util.UUID;

@Service
public class RegisterService implements Register {

    private final CustomerRepository customerRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(CustomerRepository customerRepository, VerificationTokenRepository verificationTokenRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
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

        generateToken(customer);
    }

    private void generateToken(Customer customer) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setCustomer(customer);
        verificationToken.setCreatedDate(Instant.now());

        verificationTokenRepository.save(verificationToken);
        System.out.println("Token was generated and inserted " + token);
//        return token;
    }
}
