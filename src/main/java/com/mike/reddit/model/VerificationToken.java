package com.mike.reddit.model;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;
    private String token;
    @OneToOne(fetch = FetchType.LAZY)
    private Customer customer;
    private Instant createdDate;

    public VerificationToken() {
    }

    public VerificationToken(Long verificationId, String token, Customer customer, Instant createdDate) {
        this.verificationId = verificationId;
        this.token = token;
        this.customer = customer;
        this.createdDate = createdDate;
    }

    public Long getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(Long verificationId) {
        this.verificationId = verificationId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
