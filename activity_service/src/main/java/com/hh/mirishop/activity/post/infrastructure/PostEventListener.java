package com.hh.mirishop.activity.post.infrastructure;

import com.hh.mirishop.activity.post.entity.Post;
import jakarta.persistence.PostPersist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostEventListener {

    private final MongoTemplate mongoTemplate;

    /*
    글 생성시 뉴스피드에도 생성
    */
    @PostPersist
    public void onPostCreate(Post post) {
//        Activity activity = Activity.builder()
//                .memberNumber(post.getMember().getNumber())
//                .activityType(ActivityType.POST)
//                .activityId(post.getPostId())
//                .targetPostId(post.getPostId())
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .isDeleted(false)
//                .build();
//
//        mongoTemplate.save(activity, "activities");
    }

    /*
    글 수정 시 뉴스피드 반영
    */
//    @PostUpdate
//    public void onPostUpdate(Post post) {
//        Query query = new Query(Criteria.where("activityId").is(post.getPostId())
//                .and("activityType").is(ActivityType.POST));
//        Update update = new Update();
//        update.set("updatedAt", LocalDateTime.now());
//
//        mongoTemplate.updateFirst(query, update, "activities");
//    }

    /*
    포스트 삭제는 AOP를 이용하여
    common.aspect.MongoDBSoftDeleteAspect에서 처리
    */
}

