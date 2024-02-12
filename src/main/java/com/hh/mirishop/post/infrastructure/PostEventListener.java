package com.hh.mirishop.post.infrastructure;

import com.hh.mirishop.newsfeed.domain.ActivityType;
import com.hh.mirishop.newsfeed.entity.Activity;
import com.hh.mirishop.post.entity.Post;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PostEventListener {

    private final MongoTemplate mongoTemplate;

    /*
    글 생성시 뉴스피드에도 생성
    */
    @PostPersist
    public void onPostCreate(Post post) {
        Activity activity = Activity.builder()
                .memberNumber(post.getMember().getNumber())
                .activityType(ActivityType.POST)
                .activityId(post.getPostId())
                .targetPostId(post.getPostId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        mongoTemplate.save(activity, "activities");
    }

    @PostUpdate
    public void onPostUpdate(Post post) {
        Query query = new Query(Criteria.where("activityId").is(post.getPostId())
                .and("activityType").is(ActivityType.POST));
        Update update = new Update();
        update.set("updatedAt", LocalDateTime.now());

        mongoTemplate.updateFirst(query, update, "activities");
    }

    @PostRemove
    public void onPostRemove(Post post) {
        Activity activity = Activity.builder()
                .memberNumber(post.getMember().getNumber())
                .activityType(ActivityType.POST)
                .activityId(post.getPostId())
                .isDeleted(false)
                .build();

        mongoTemplate.save(activity, "activity");
    }
}

