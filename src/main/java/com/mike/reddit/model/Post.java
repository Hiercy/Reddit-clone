package com.mike.reddit.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @NotBlank(message = "Post name cannot be empty or null")
    private String postName;
    @Nullable
    private String url;
    @Nullable
    @Lob
    private String description;
    private int voteCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer", referencedColumnName = "customerId")
    private Customer customer;
    private Instant createdDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subreddit", referencedColumnName = "subredditId")
    private Subreddit subReddit;

    public Long getPostId() {
        return postId;
    }

    public String getPostName() {
        return postName;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Subreddit getSubReddit() {
        return subReddit;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public static class Builder {
        private String postName;

        private String url;
        private String description;
        private int voteCount;
        private Customer customer;
        private Instant createdDate;
        private Subreddit subReddit;

        public Builder setPostName(String postName) {
            this.postName = postName;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setVoteCount(int voteCount) {
            this.voteCount = voteCount;
            return this;
        }

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder setTimeNow() {
            this.createdDate = Instant.now();
            return this;
        }

        public Builder setTimeNow(Instant createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder setSubReddit(Subreddit subReddit) {
            this.subReddit = subReddit;
            return this;
        }

        public Post build() {
            return new Post(this);
        }
    }

    protected Post() {
    }

    private Post(Builder builder) {
        this.postName = builder.postName;
        this.url = builder.url;
        this.description = builder.description;
        this.voteCount = builder.voteCount;
        this.customer = builder.customer;
        this.createdDate = builder.createdDate;
        this.subReddit = builder.subReddit;
    }
}
