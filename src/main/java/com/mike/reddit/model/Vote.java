package com.mike.reddit.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;
    private VoteType voteType;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post", referencedColumnName = "postId")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer", referencedColumnName = "customerId")
    private Customer customer;

    public Long getVoteId() {
        return voteId;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public Post getPost() {
        return post;
    }

    public Customer getCustomer() {
        return customer;
    }

    public static class Builder {
        private Long voteId;
        private VoteType voteType;
        private Post post;
        private Customer customer;

        public Builder setId(Long voteId) {
            this.voteId = voteId;
            return this;
        }

        public Builder setVoteType(VoteType voteType) {
            this.voteType = voteType;
            return this;
        }

        public Builder setPost(Post post) {
            this.post = post;
            return this;
        }

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Vote build() {
            return new Vote(this);
        }
    }

    protected Vote() {
    }

    private Vote(Builder builer) {
        this.voteId = builer.voteId;
        this.voteType = builer.voteType;
        this.post = builer.post;
        this.customer = builer.customer;
    }
}
