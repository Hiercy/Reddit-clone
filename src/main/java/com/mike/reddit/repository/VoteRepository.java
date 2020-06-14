package com.mike.reddit.repository;

import com.mike.reddit.model.Customer;
import com.mike.reddit.model.Post;
import com.mike.reddit.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndCustomerOrderByVoteIdDesc(Post post, Customer customer);
}

