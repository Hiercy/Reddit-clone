package com.mike.reddit.model;

import com.mike.reddit.exceptions.SpringRedditException;

import java.util.Arrays;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1);

    private final int vote;

    VoteType(int vote) {
        this.vote = vote;
    }

    public static VoteType lookup(Integer vote) {
        return Arrays.stream(VoteType.values())
                .filter(value -> value.getVote().equals(vote))
                .findAny()
                .orElseThrow(() -> new SpringRedditException("Vote not found"));
    }

    public Integer getVote() {
        return vote;
    }
}
