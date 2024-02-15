package com.hh.mirishop.activity.comment.infrastructure;

import com.hh.mirishop.activity.comment.entity.Comment;
import jakarta.persistence.PostPersist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentEventListener {

    private final MongoTemplate mongoTemplate;

    /*
    댓글 생성시 뉴스피드에 생성
    */
    @PostPersist
    public void onCommentCreate(Comment comment) {
//        Activity activity =  Activity.builder()
//                .memberNumber(comment.getMemberNumber())
//                .activityType(ActivityType.COMMENT)
//                .activityId(comment.getCommentId())
//                .targetPostId(comment.getPost().getPostId())
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .isDeleted(false)
//                .build();
//
//        mongoTemplate.save(activity, "activities");
    }

    /*
    댓글 삭제시 뉴스피드에 생성
    */
}

