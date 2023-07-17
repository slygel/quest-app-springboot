package com.example.questapp.service;

import com.example.questapp.entity.Like;
import com.example.questapp.entity.User;
import com.example.questapp.entity.Post;
import com.example.questapp.repos.LikeRepository;
import com.example.questapp.requests.LikeCreateRequest;
import com.example.questapp.responses.LikeResponse;

import java.util.*;
import java.util.stream.*;

import org.springframework.stereotype.Service;

@Service
public class LikeService {
    
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;


    public LikeService(LikeRepository likeRepository, UserService userService, 
			PostService postService) {
		this.likeRepository = likeRepository;
		this.userService = userService;
		this.postService = postService;
	}

	public List<Like> getAllLikes(){
		return likeRepository.findAll(); 
	}

    public List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {
		List<Like> list;
		if(userId.isPresent() && postId.isPresent()) {
			list = likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
		}else if(userId.isPresent()) {
			list = likeRepository.findByUserId(userId.get());
		}else if(postId.isPresent()) {
			list = likeRepository.findByPostId(postId.get());
		}else
			list = likeRepository.findAll();
		return list.stream().map(like -> new LikeResponse(like)).collect(Collectors.toList());
	}

    public Like getOneLikeById(Long LikeId) {
		return likeRepository.findById(LikeId).orElse(null);
	}

    public Like createOneLike(LikeCreateRequest request) {
		User user = userService.getUserById(request.getUserId());
		Post post = postService.getPostById(request.getPostId());
		if(user != null && post != null) {
			Like likeToSave = new Like();
			likeToSave.setId(request.getId());
			likeToSave.setPost(post);
			likeToSave.setUser(user);
			return likeRepository.save(likeToSave);
		}else		
			return null;
	}

    public void deleteOneLikeById(Long likeId) {
		likeRepository.deleteById(likeId);
	}
	
}
