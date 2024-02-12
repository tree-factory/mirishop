package com.hh.mirishop.newsfeed.dto;

import com.hh.mirishop.newsfeed.domain.ActivityType;
import com.hh.mirishop.newsfeed.entity.Activity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ActivityResponse {

    private String id;
    private Long memberNumber;
    private ActivityType activityType;
    private Long activityId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static ActivityResponse fromActivity(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getMemberNumber(),
                activity.getActivityType(),
                activity.getActivityId(),
                activity.getTitle(),
                activity.getContent(),
                activity.getCreatedAt()
        );
    }
}
