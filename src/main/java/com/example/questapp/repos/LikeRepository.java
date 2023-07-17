package com.example.questapp.repos;

import java.util.*;

import com.example.questapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.questapp.entity.Like;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUserIdAndPostId(Long userId, Long postId);

	List<Like> findByUserId(Long userId);

	List<Like> findByPostId(Long postId);

	@Query(value = "select * from p_like where post_id in :postIds  limit 5" , nativeQuery = true)
	List<Object> findUserLikesByPostId(@Param("postIds") List<Long> postIds);

}
