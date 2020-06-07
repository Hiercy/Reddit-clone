package com.mike.reddit.repository;

import com.mike.reddit.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    @Query(value = "SELECT * FROM token WHERE customer_customer_id = ?1", nativeQuery = true)
    VerificationToken findByCustomerId(Long id);
}
