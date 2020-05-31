package com.mike.reddit.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Entity
public class Subreddit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subredditId;
    @NotBlank(message = "Community title cannot be empty")
    private String name;
    @NotBlank(message = "Community description cannot be empty")
    private String description;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> posts;
    private Instant createdDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    public Long getSubredditId() {
        return subredditId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public static class Builder {
        private Long subredditId;
        private String name;
        private String description;
        private List<Post> posts;
        private Instant createdDate;
        private Customer customer;

        public Builder setSibredditId(Long subredditId) {
            this.subredditId = subredditId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPosts(List<Post> posts) {
            this.posts = posts;
            return this;
        }

        public Builder setCreatedDate() {
            this.createdDate = Instant.now();
            return this;
        }

        public Builder setCreatedDate(Instant createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Subreddit build() {
            return new Subreddit(this);
        }
    }

    protected Subreddit() {
    }

    private Subreddit(Builder builder) {
        this.subredditId = builder.subredditId;
        this.name = builder.name;
        this.description = builder.description;
        this.posts = builder.posts;
        this.createdDate = builder.createdDate;
        this.customer = builder.customer;
    }
}
