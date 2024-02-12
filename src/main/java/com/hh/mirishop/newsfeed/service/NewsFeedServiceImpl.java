package com.hh.mirishop.newsfeed.service;

import com.hh.mirishop.auth.domain.UserDetailsImpl;
import com.hh.mirishop.follow.entity.Follow;
import com.hh.mirishop.follow.repository.FollowRepository;
import com.hh.mirishop.newsfeed.dto.ActivityResponse;
import com.hh.mirishop.newsfeed.entity.Activity;
import com.hh.mirishop.newsfeed.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsFeedServiceImpl implements NewsFeedService {

    private final FollowRepository followRepository;

    private final ActivityRepository activityRepository;

    private final MongoTemplate mongoTemplate;

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> getNewsfeedForMember(int page, int size, UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Long currentMemberNumber = userDetails.getNumber();

        // 유저의 팔로잉 사용자 조회
        List<Follow> followings = followRepository.findAllByFollowerId(currentMemberNumber);

        List<Long> followingMemberNumbers = followings.stream()
                .map(follow -> follow.getFollowing().getNumber())
                .toList();

        // 팔로잉하는 사용자들의 활동을 조회
        Query query = new Query(Criteria.where("authorId").in(followingMemberNumbers))
                .with(pageable);

        List<Activity> activities = mongoTemplate.find(query, Activity.class, "activities");
        long total = mongoTemplate.count(query, Activity.class, "activities");

        List<ActivityResponse> activityResponses = activities.stream()
                .map(ActivityResponse::fromActivity)
                .toList();

        return new PageImpl<>(activityResponses, pageable, total);
    }
}