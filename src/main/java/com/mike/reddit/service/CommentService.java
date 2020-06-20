package com.mike.reddit.service;

import com.mike.reddit.dto.CommentDto;
import com.mike.reddit.exceptions.SpringRedditException;
import com.mike.reddit.model.Comment;
import com.mike.reddit.model.Customer;
import com.mike.reddit.model.Post;
import com.mike.reddit.repository.CommentRepository;
import com.mike.reddit.repository.CustomerRepository;
import com.mike.reddit.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CustomerRepository customerRepository;
    private final AuthService authService;
    private final MailService mailService;

    @Autowired
    public CommentService(PostRepository postRepository, CommentRepository commentRepository, CustomerRepository customerRepository, AuthService authService, MailService mailService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.customerRepository = customerRepository;
        this.authService = authService;
        this.mailService = mailService;
    }

    public void create(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("Cannot find post with id = " + commentDto.getPostId() + "!"));

        Comment comment = dtoToComment(commentDto, post, authService.getCustomer());
        commentRepository.save(comment);

        String message = post.getCustomer().getUsername() + " commented your post.";
        sendNotification(message, post.getCustomer());
    }


    public List<CommentDto> getAllCommentsFromUser(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("Cannot find user by username = " + username + "!"));
        return commentRepository
                .findAllByCustomer(customer)
                .stream()
                .map(this::commentToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsFromPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SpringRedditException("Cannot find post with id = " + postId + "!"));
        return commentRepository
                .findByPost(post)
                .stream()
                .map(this::commentToDto)
                .collect(Collectors.toList());
    }

    private void sendNotification(String message, Customer customer) {
        mailService.send(customer.getEmail(), "Your post commented", message);
    }

    private Comment dtoToComment(CommentDto commentDto, Post post, Customer customer) {
        if (commentDto == null || post == null || customer == null) {
            throw new SpringRedditException("Some data is null!"
                    + "\ncomment dto = " + commentDto
                    + "\npost = " + post
                    + "\ncustomer = " + customer);
        }
        return new Comment().builder()
                .text(commentDto.getText())
                .post(post)
                .customer(customer)
                .createdDate(Instant.now())
                .build();
    }

    private CommentDto commentToDto(Comment comment) {
        if (comment == null) {
            throw new SpringRedditException("Comment is null!");
        }
        return new CommentDto().builder()
                .id(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .text(comment.getText())
                .username(comment.getCustomer().getUsername())
                .createDate(comment.getCreatedDate())
                .build();
    }
}
