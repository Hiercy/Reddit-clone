package com.mike.reddit.dto;

public class AuthenticationResponse {
    private final String authenticationToken;
    private final String username;

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public String getUsername() {
        return username;
    }

    public static class Builder {
        private String authenticationToken;
        private String username;

        public Builder setAuthenticationToken(String authenticationToken) {
            this.authenticationToken = authenticationToken;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public AuthenticationResponse build() {
            return new AuthenticationResponse(this);
        }
    }

    private AuthenticationResponse(Builder builder) {
        this.authenticationToken = builder.authenticationToken;
        this.username = builder.username;
    }
}
