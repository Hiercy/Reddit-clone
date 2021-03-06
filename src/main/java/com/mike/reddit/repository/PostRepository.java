package com.mike.reddit.repository;

import com.mike.reddit.model.Customer;
import com.mike.reddit.model.Post;
import com.mike.reddit.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByCustomer(Customer customer);

    List<Post> findAllBySubReddit(Subreddit subreddit);
}
