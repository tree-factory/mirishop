package com.hh.newsfeed.newsfeed.service;

import com.hh.newsfeed.auth.domain.UserDetailsImpl;
import com.hh.newsfeed.comment.service.CommentService;
import com.hh.newsfeed.follow.entity.Follow;
import com.hh.newsfeed.follow.repository.FollowRepository;
import com.hh.newsfeed.like.domain.LikeType;
import com.hh.newsfeed.like.entity.Like;
import com.hh.newsfeed.newsfeed.domain.ActivityType;
import com.hh.newsfeed.newsfeed.dto.ActivityResponse;
import com.hh.newsfeed.newsfeed.entity.Activity;
import com.hh.newsfeed.post.repository.PostRepository;
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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class NewsFeedServiceImpl implements NewsFeedService {

    private final CommentService commentService;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final MongoTemplate mongoTemplate;

    /*
    Todo: query문을 어떻게 최적화 할지 고민
    */
    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponse> getNewsfeedForMember(int page, int size, UserDetailsImpl userDetails) {
        Long currentMemberNumber = userDetails.getNumber();

        // 팔로우 유저에 대한 정보
        List<Activity> activitiesForFollows = getActivitiesForFollowing(currentMemberNumber);
        // 나의 글에 달린 댓글과 좋아요에 대한 정보
        List<Activity> activitiesForMyPosts = getActivitiesForMyPosts(currentMemberNumber);
        // 다른 글에 달은 댓글과 좋아요에 대한 정보
        List<Activity> activitiesForOtherPosts = getActivitiesForOtherPosts(currentMemberNumber);
        // 대댓글과 대댓글에 대한 좋아요에 대한 정보
        List<Activity> activitiesForReplies = getActivitiesForReplies(currentMemberNumber);

        //
        List<Activity> activities = Stream.of(activitiesForFollows, activitiesForMyPosts, activitiesForOtherPosts,
                        activitiesForReplies)
                .flatMap(Collection::stream)
                .toList();

        int totalActivities = activities.size();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        List<ActivityResponse> activityResponses = activities.stream().map(ActivityResponse::fromActivity).toList();

        return new PageImpl<>(activityResponses, pageable, totalActivities);
    }

    private List<Activity> getActivitiesForFollowing(Long currentMemberNumber) {
        List<Follow> followings = followRepository.findAllByFollowerId(currentMemberNumber);
        List<Long> followingMemberNumbers = followings.stream()
                .map(follow -> follow.getFollowing().getNumber())
                .toList();

        Query queryForFollows = new Query(Criteria.where("memberNumber").in(followingMemberNumbers)
                .andOperator(
                        Criteria.where("isDeleted").is(false)
                ));

        return mongoTemplate.find(queryForFollows, Activity.class, "activities");
    }

    private List<Activity> getActivitiesForMyPosts(Long currentMemberNumber) {
        List<Long> myPostIds = postRepository.findByMemberNumberAndIsDeletedFalse(currentMemberNumber);

        Query queryForMyPosts = new Query(Criteria.where("targetPostId").in(myPostIds)
                .andOperator(
                        Criteria.where("activityType").in(ActivityType.COMMENT, ActivityType.LIKE),
                        Criteria.where("isDeleted").is(false)
                ));

        return mongoTemplate.find(queryForMyPosts, Activity.class, "activities");
    }

    private List<Activity> getActivitiesForOtherPosts(Long currentMemberNumber) {
        Query queryForOtherPosts = new Query(Criteria.where("memberNumber").is(currentMemberNumber)
                .andOperator(
                        Criteria.where("activityType").in(ActivityType.COMMENT, ActivityType.LIKE),
                        Criteria.where("isDeleted").is(false)
                ));
        return mongoTemplate.find(queryForOtherPosts, Activity.class, "activities");
    }

    private List<Activity> getActivitiesForReplies(Long currentMemberNumber) {
        System.out.println(3);
        List<Long> commentIds = commentService.findCommentIdsByMemberNumber(currentMemberNumber);

        Query queryForRepliesAndLikes = new Query(Criteria.where("parentCommentId").in(commentIds)
                .andOperator(
                        Criteria.where("activityType").in(ActivityType.COMMENT, ActivityType.LIKE),
                        Criteria.where("isDeleted").is(false)
                ));

        return mongoTemplate.find(queryForRepliesAndLikes, Activity.class, "activities");
    }

    @Override
    @Transactional
    public void createActivityForLike(Like like) {
        Long postId = findRelatedPostIdForLike(like);

        Activity activity = Activity.builder()
                .memberNumber(like.getMember().getNumber())
                .activityType(ActivityType.LIKE)
                .activityId(like.getLikeId())
                .targetPostId(postId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        // MongoDB에 Activity 문서 저장
        mongoTemplate.save(activity, "activities");
    }

    @Override
    @Transactional
    public void deleteActivityForUnlike(Like like) {
        Long postId = findRelatedPostIdForLike(like);

        // 타입이 LIKE 인 것 중에서 postId를 기준으로 삭제
        Query query = new Query(Criteria
                .where("activityType").is(ActivityType.LIKE)
                .and("activityId").is(postId));
        mongoTemplate.remove(query, Activity.class, "activities");
    }

    @Transactional(readOnly = true)
    private Long findRelatedPostIdForLike(Like like) {
        if (like.getLikeType() == LikeType.POST) {
            return like.getItemId();
        } else if (like.getLikeType() == LikeType.COMMENT) {
            return commentService.findPostIdByCommentId(like.getItemId());
        }
        return null;
    }
}