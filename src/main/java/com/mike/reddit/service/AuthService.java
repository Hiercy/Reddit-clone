package com.mike.reddit.service;

import com.mike.reddit.dto.AuthenticationResponse;
import com.mike.reddit.dto.LoginRequest;
import com.mike.reddit.dto.RegisterRequest;
import com.mike.reddit.exceptions.SpringRedditException;
import com.mike.reddit.jwt.JwtProvider;
import com.mike.reddit.model.Customer;
import com.mike.reddit.model.VerificationToken;
import com.mike.reddit.repository.CustomerRepository;
import com.mike.reddit.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements Register {

    private final CustomerRepository customerRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthService(CustomerRepository customerRepository, VerificationTokenRepository verificationTokenRepository, PasswordEncoder passwordEncoder, MailSender mailSender, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.customerRepository = customerRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    @Transactional
    public String register(RegisterRequest registerRequest) {
        Customer customer = new Customer();
        customer.setUsername(registerRequest.getUsername());
        customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        customer.setEmail(registerRequest.getEmail());
        customer.setCreatedDate(Instant.now());
        customer.setEnabled(false);

        customerRepository.save(customer);

        String token = generateToken(customer);
        sendVerificationMail(customer, token);
        return token;
    }

    @Override
    public void verify(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verifyUser(verificationToken.orElseThrow(() -> new SpringRedditException("Invalid token")));
    }

    @Transactional
    public void verifyUser(VerificationToken verificationToken) {
        String username = verificationToken.getCustomer().getUsername();
        Customer customer = customerRepository
                .findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("User with username " + username + " not found"));

        customer.setEnabled(true);

        customerRepository.save(customer);
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
            String message = String.format("Hello, %s! Welcome to Reddit-clone. Please visit next link: http://localhost:8080/auth/activate/%s",
                    customer.getUsername(),
                    token);

            mailSender.send(customer.getEmail(), "Activation code from Reddit-clone", message);
        }
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        return new AuthenticationResponse.Builder()
                .setAuthenticationToken(token)
                .setUsername(loginRequest.getUsername())
                .build();
    }
}
