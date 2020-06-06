package com.mike.reddit.service;

import com.mike.reddit.dto.RegisterRequest;

public interface Register {

    String register(RegisterRequest registerRequest);

    void verify(String token);
}
