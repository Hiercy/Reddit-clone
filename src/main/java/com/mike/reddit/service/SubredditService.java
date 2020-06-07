package com.mike.reddit.service;

import com.mike.reddit.dto.SubredditDto;
import com.mike.reddit.exceptions.SpringRedditException;
import com.mike.reddit.model.Subreddit;
import com.mike.reddit.repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubredditService {

    private final SubredditRepository subredditRepository;

    @Autowired
    public SubredditService(SubredditRepository subredditRepository) {
        this.subredditRepository = subredditRepository;
    }

    @Transactional
    public SubredditDto create(SubredditDto subredditDto) {
        Subreddit save = subredditRepository.save(new Subreddit.Builder()
                .setName(subredditDto.getTitle())
                .setDescription(subredditDto.getDescription())
                .setSibredditId(subredditDto.getId())
                .setCreatedDate()
                .build());
        subredditDto.setId(save.getSubredditId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubredditDto getOne(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("Cannot find subreddit with this id"));
        return entityToDto(subreddit);
    }

    private SubredditDto entityToDto(Subreddit subreddit) {
        return new SubredditDto.Builder()
                .setId(subreddit.getSubredditId())
                .setTitle(subreddit.getName())
                .setDescription(subreddit.getDescription())
                .setNumOfPosts(subreddit.getPosts().size())
                .build();
    }
}
