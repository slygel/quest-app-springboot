package com.example.questapp.service;

import java.util.*;

import com.example.questapp.entity.Comment;
import com.example.questapp.entity.Like;
import com.example.questapp.repos.CommentRepository;
import com.example.questapp.repos.LikeRepository;
import com.example.questapp.repos.PostRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.questapp.entity.User;
import com.example.questapp.repos.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    private final LikeRepository likeRepository;

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    public UserService(UserRepository userRepository,
                       LikeRepository likeRepository,
                       CommentRepository commentRepository,
                       PostRepository postRepository) {
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User saveOneUser(User newUser) {
        return userRepository.save(newUser);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long id , User newUser){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User userUpdate = userOptional.get();
            userUpdate.setUsername(newUser.getUsername());
            userUpdate.setPassword(newUser.getPassword());
            userRepository.save(userUpdate);
            return userUpdate;
        }else{
            return null;
        }
    }

    public void deleteById(Long userId) {
        try {
            userRepository.deleteById(userId);
        }catch(EmptyResultDataAccessException e) {
            System.out.println("User "+userId+" doesn't exist");
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<Object> getUserActivity(Long userId){
        List<Long> postIds = postRepository.findTopByUserId(userId);
        if(postIds.isEmpty())
            return null;
        List<Object> comments = commentRepository.findUserCommentsByPostId(postIds);
        List<Object> likes = likeRepository.findUserLikesByPostId(postIds);
        List<Object> result = new ArrayList<>();
        result.addAll(comments);
        result.addAll(likes);

        return result;
    }
}
