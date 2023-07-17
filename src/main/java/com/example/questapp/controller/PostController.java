package com.example.questapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.questapp.entity.Post;
import com.example.questapp.requests.PostCreateRequest;
import com.example.questapp.requests.PostUpdateRequest;
import com.example.questapp.responses.PostResponse;
import com.example.questapp.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {
    
    @Autowired
    private PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping
    public List<PostResponse> getAllPosts(@RequestParam Optional<Long> userId){
        return postService.getAllPosts(userId);
    }

    @PostMapping
    public Post createPost(@RequestBody PostCreateRequest newPostRequest){
        return postService.createPost(newPostRequest);
    }

    @GetMapping("/{postId}")
    public PostResponse getPostById(@PathVariable Long postId){
        return postService.getOnePostByIdWithLikes(postId);
    }

    @PutMapping("/{postId}")
    public Post updatePost(@PathVariable Long postId , 
                            @RequestBody PostUpdateRequest updatePost)
    {
        return postService.updatePostById(postId ,updatePost);
    }

    @DeleteMapping("/{postId}")
    public String deletePostById(@PathVariable Long postId){
        return postService.deletePostById(postId);
    }
}
