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
    private Long activityId; // 활동 고유 ID (예: 게시글 ID, 댓글 ID)

    @Field("authorId")
    private Long authorId; // 활동 작성자 ID

    @Field("title")
    private String title;

    @Field("content")
    private String content; // 활동 내용

    @Field("createdAt")
    private LocalDateTime createdAt; // 생성 시간

    @Field("updatedAt")
    private LocalDateTime updatedAt; // 수정 시간

    private Long targetPostId;
}