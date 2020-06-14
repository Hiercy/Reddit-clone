package com.mike.reddit.service;

import com.mike.reddit.dto.VoteDto;
import com.mike.reddit.exceptions.SpringRedditException;
import com.mike.reddit.model.Post;
import com.mike.reddit.model.Vote;
import com.mike.reddit.model.VoteType;
import com.mike.reddit.repository.PostRepository;
import com.mike.reddit.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Autowired
    public VoteService(VoteRepository voteRepository, PostRepository postRepository, AuthService authService) {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.authService = authService;
    }

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("Cannot find post with id " + voteDto.getPostId() + "!"));

        // Check if the user already voted this post
        checkVote(post, voteDto);

        voteRepository.save(dtoToVote(voteDto, post));
        postRepository.save(post);
    }

    private void checkVote(Post post, VoteDto voteDto) {
        Optional<Vote> usersVoteOnCurrentPost = voteRepository.findTopByPostAndCustomerOrderByVoteIdDesc(post, authService.getCustomer());
        if (usersVoteOnCurrentPost.isPresent() && usersVoteOnCurrentPost.get().getVoteType().equals(voteDto.getVoteType())) {
            /*
             * TODO: Correct it.
             *  Now - if user hit upvote voteCount = 1. If after upvote user hit downvote viewCount = 0.
             *  How it should be - if user hit upvote voteCount = 1. If after upvote user hit downvote viewCount = -1.
             *  If user several time hit downvote it should be voteCount = from -1 to 0. The same for upvote.
             */

            throw new SpringRedditException("You have already voted post");
        } else if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else if (VoteType.DOWNVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() - 1);
        } else {
            throw new SpringRedditException("Something went wrong while vote the post");
        }
    }

    private Vote dtoToVote(VoteDto voteDto, Post post) {
        return new Vote().builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .customer(authService.getCustomer())
                .build();
    }
}
