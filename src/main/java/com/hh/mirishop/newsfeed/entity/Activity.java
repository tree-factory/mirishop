package com.hh.mirishop.newsfeed.entity;

import com.hh.mirishop.newsfeed.domain.ActivityType;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "Activities")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Activity {

    @Id
    private String id;

    @Field("memberNumber")
    private Long memberNumber;

    @Field("activityType")
    private ActivityType activityType; // 활동 유형: post/comment/like

    @Field("activityId")
    private Long activityId; // 활동 고유 ID (게시글 ID, 댓글 ID, 좋아요 ID)

    @Field("targetPostId")
    private Long targetPostId;

    @Field("createdAt")
    private LocalDateTime createdAt;

    @Field("updatedAt")
    private LocalDateTime updatedAt;

    @Field("is_deleted")
    private Boolean isDeleted;
}