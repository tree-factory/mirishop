package com.hh.mirishop.comment.infrastructure;

import com.hh.mirishop.comment.entity.Comment;
import com.hh.mirishop.newsfeed.domain.ActivityType;
import com.hh.mirishop.newsfeed.entity.Activity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CommentEventListener {

    private final MongoTemplate mongoTemplate;

    /*
    댓글 생성시 뉴스피드에 생성
    */
    @PostPersist
    public void onCommentCreate(Comment comment) {
        Activity activity =  Activity.builder()
                .memberNumber(comment.getMember().getNumber())
                .activityType(ActivityType.COMMENT)
                .activityId(comment.getCommentId())
                .targetPostId(comment.getPost().getPostId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        mongoTemplate.save(activity, "activities");
    }

    /*
    댓글 삭제시 뉴스피드에 생성
    */
    @PostRemove
    public void onCommentRemove(Comment comment) {
        Query query = new Query();
        // activityType에서 타입이 COMMENT인 것중 commentID를 확인하여 삭제
        query.addCriteria(Criteria.where("activityId").is(comment.getCommentId())
                .and("activityType").is(ActivityType.COMMENT));

        mongoTemplate.remove(query, Activity.class, "activity");
    }
}

