package com.mike.reddit.service;

import com.mike.reddit.dto.RegisterRequest;

public interface Register {

    void register(RegisterRequest registerRequest);

    void verify(String token);
}
