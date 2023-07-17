package com.example.questapp.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.questapp.entity.Like;
import com.example.questapp.requests.LikeCreateRequest;
import com.example.questapp.responses.LikeResponse;
import com.example.questapp.service.LikeService;

@RequestMapping("/likes")
@RestController
public class LikeController {
    
    @Autowired
    private LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
	public List<LikeResponse> getAllLikes(@RequestParam Optional<Long> userId, 
			                              @RequestParam Optional<Long> postId) {
		return likeService.getAllLikesWithParam(userId, postId);
	}

    @PostMapping
    public Like createOnLike(@RequestBody LikeCreateRequest request){
        return likeService.createOneLike(request);
    }

    @GetMapping("/{likeId}")
	public Like getOneLike(@PathVariable Long likeId) {
		return likeService.getOneLikeById(likeId);
	}

    @DeleteMapping("/{likeId}")
	public void deleteOneLike(@PathVariable Long likeId) {
		likeService.deleteOneLikeById(likeId);
	}
}
