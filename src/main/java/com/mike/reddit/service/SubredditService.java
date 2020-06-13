package com.mike.reddit.service;

import com.mike.reddit.dto.SubredditDto;
import com.mike.reddit.exceptions.SpringRedditException;
import com.mike.reddit.model.Subreddit;
import com.mike.reddit.repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
        Subreddit save = subredditRepository.save(dtoToSubreddit(subredditDto));
        subredditDto.setId(save.getSubredditId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubredditDto getOne(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("Cannot find subreddit with this id"));
        return entityToDto(subreddit);
    }

    private Subreddit dtoToSubreddit(SubredditDto subredditDto) {
        return new Subreddit().builder()
                .name(subredditDto.getTitle())
                .description(subredditDto.getDescription())
                .subredditId(subredditDto.getId())
                .createdDate(Instant.now())
                .build();
    }

    private SubredditDto entityToDto(Subreddit subreddit) {
        return new SubredditDto().builder()
                .id(subreddit.getSubredditId())
                .title(subreddit.getName())
                .description(subreddit.getDescription())
                .numOfPosts(subreddit.getPosts().size())
                .build();
    }
}
