package com.example.questapp.responses;

import com.example.questapp.entity.Comment;
import lombok.Data;

@Data
public class CommentResponse {

    Long id;
    Long userId;
    String username;
    String text;

    public CommentResponse(Comment entity) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.username = entity.getUser().getUsername();
        this.text = entity.getText();
    }
}
