package com.mike.reddit.controller;

import com.mike.reddit.dto.CommentDto;
import com.mike.reddit.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
public class CommentController {

    @Autowired
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto) {
        commentService.create(commentDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/byPost/{id}")
    public ResponseEntity<List<CommentDto>> getAllCommentsFromPost(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsFromPost(id));
    }

    @GetMapping("/byUser/{username}")
    public ResponseEntity<List<CommentDto>> getAllCommentsFromUser(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsFromUser(username));
    }
}
