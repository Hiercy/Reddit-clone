package com.mike.reddit.repository;

import com.mike.reddit.model.Comment;
import com.mike.reddit.model.Customer;
import com.mike.reddit.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCustomer(Customer customer);

    List<Comment> findByPost(Post post);
}
