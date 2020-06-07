package com.mike.reddit.dto;

public class SubredditDto {

    private Long id;
    private String title;
    private String description;
    private Integer numOfPosts;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static class Builder {
        private Long id;
        private String title;
        private String description;
        private Integer numOfPosts;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setNumOfPosts(Integer numOfPosts) {
            this.numOfPosts = numOfPosts;
            return this;
        }

        public SubredditDto build() {
            return new SubredditDto(this);
        }
    }

    protected SubredditDto() {
    }

    private SubredditDto(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.numOfPosts = builder.numOfPosts;
    }
}
