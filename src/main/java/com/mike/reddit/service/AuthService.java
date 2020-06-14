package com.mike.reddit.service;

import com.mike.reddit.dto.AuthenticationResponse;
import com.mike.reddit.dto.LoginDto;
import com.mike.reddit.dto.RegisterDto;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthService(CustomerRepository customerRepository, VerificationTokenRepository verificationTokenRepository, PasswordEncoder passwordEncoder, MailService mailService, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.customerRepository = customerRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public String register(RegisterDto registerDto) {
        Optional<Customer> check = customerRepository.findByUsername(registerDto.getUsername());
        if (check.isPresent() && registerDto.getUsername().equals(check.get().getUsername())) {
            Customer customer = customerRepository.findByUsername(registerDto.getUsername())
                    .orElseThrow(() -> new SpringRedditException("User not exist"));
            VerificationToken verificationToken = verificationTokenRepository.findByCustomerId(customer.getCustomerId());

            return verificationToken.getToken();
        }
        Customer customer = new Customer();
        customer.setUsername(registerDto.getUsername());
        customer.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        customer.setEmail(registerDto.getEmail());
        customer.setCreatedDate(Instant.now());
        customer.setEnabled(false);

        customerRepository.save(customer);

        String token = generateToken(customer);
        sendVerificationMail(customer, token);
        return token;
    }

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

            mailService.send(customer.getEmail(), "Activation code from Reddit-clone", message);
        }
    }

    public AuthenticationResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        return new AuthenticationResponse().builder()
                .authenticationToken(token)
                .username(loginDto.getUsername())
                .build();
    }

    @Transactional(readOnly = true)
    public Customer getCustomer() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return customerRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }
}
