package com.mike.reddit.service;

import com.mike.reddit.dto.PostRequestDto;
import com.mike.reddit.dto.PostResponseDto;
import com.mike.reddit.exceptions.SpringRedditException;
import com.mike.reddit.model.Customer;
import com.mike.reddit.model.Post;
import com.mike.reddit.model.Subreddit;
import com.mike.reddit.repository.CustomerRepository;
import com.mike.reddit.repository.PostRepository;
import com.mike.reddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PostService {

    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final SubredditRepository subredditRepository;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final AuthService authService;

    public Post createPost(PostRequestDto postRequestDto) {
        Subreddit subreddit = subredditRepository.findByName(postRequestDto.getSubredditName())
                .orElseThrow(() -> new SpringRedditException("Cannot find subreddit with name " + postRequestDto.getSubredditName() + "!"));

        return postRepository.save(dtoToPost(postRequestDto, subreddit, authService.getCustomer()));
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("Cannot find post with id " + id + "!"));

        return postToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
        return postRepository
                .findAll()
                .stream()
                .map(this::postToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("Cannot find user with username = " + username + "!"));

        return postRepository.findAllByCustomer(customer)
                .stream()
                .map(this::postToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("Cannot find subreddit by this id " + id + "!"));

        return postRepository
                .findAllBySubReddit(subreddit)
                .stream()
                .map(this::postToDto)
                .collect(Collectors.toList());
    }

    private Post dtoToPost(PostRequestDto postRequestDto, Subreddit subreddit, Customer customer) {
        return new Post().builder()
                .postId(postRequestDto.getId())
                .postName(postRequestDto.getPostName())
                .url(postRequestDto.getUrl())
                .description(postRequestDto.getDescription())
                .voteCount(0)
                .customer(customer)
                .createdDate(Instant.now())
                .subReddit(subreddit)
                .build();
    }

    private PostResponseDto postToDto(Post post) {
        return new PostResponseDto().builder()
                .id(post.getPostId())
                .username(getUsernameByPost(post))
                .subredditName(getSubredditNameByPost(post))
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .build();
    }

    private String getSubredditNameByPost(Post post) {
        if (post == null) {
            throw new SpringRedditException("Something went wrong with post in getSubredditNameByPost()!");
        }

        Subreddit subreddit = post.getSubReddit();
        if (subreddit == null) {
            throw new SpringRedditException("Cannot find subreddit of this post in getSubredditNameByPost()!");
        }

        String subredditName = subreddit.getName();
        if (subredditName == null) {
            throw new SpringRedditException("Cannot find subreddit name in getSubredditNameByPost()!");
        }

        return subredditName;
    }

    private String getUsernameByPost(Post post) {
        if (post == null) {
            throw new SpringRedditException("Something went wrong with post in getUsernameByPost()!");
        }

        Customer customer = post.getCustomer();
        if (customer == null) {
            throw new SpringRedditException("Cannot find customer in this post!");
        }

        String username = customer.getUsername();
        if (username == null) {
            throw new SpringRedditException("User haven't username?!");
        }

        return username;
    }
}
