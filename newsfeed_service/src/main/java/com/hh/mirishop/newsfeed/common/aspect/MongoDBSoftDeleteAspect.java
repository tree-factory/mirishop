package com.hh.newsfeed.common.aspect;

import com.hh.newsfeed.newsfeed.domain.ActivityType;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MongoDBSoftDeleteAspect {

    private final MongoTemplate mongoTemplate;

    // @SoftDelete 어노테이션이 적용된 메소드 중 post service 영역의 포인트컷
    @Pointcut("@annotation(org.hibernate.annotations.SoftDelete) && within(com.hh.mirishop.post.service..*)")
    public void deletePostMethod() {
    }

    // @SoftDelete 어노테이션이 적용된 메소드 중 comment service 영역의 포인트컷
    @Pointcut("@annotation(org.hibernate.annotations.SoftDelete) && within(com.hh.mirishop.comment.service..*)")
    public void deleteCommentMethod() {
    }

    // 메서드 실행 후 소프트 삭제 처리
    @AfterReturning(pointcut = "deletePostMethod()")
    public void afterPostSoftDelete(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (args.length >= 1 && args[0] instanceof Long) {
            Query query = new Query(
                    Criteria.where("activityId").is(args[0]).and("activityType").is(ActivityType.POST));
            Update update = new Update().set("is_deleted", true);
            mongoTemplate.updateFirst(query, update, "activities");
        }
    }

    // 메서드 실행 후 소프트 삭제 처리
    @AfterReturning(pointcut = "deleteCommentMethod()")
    public void afterCommentSoftDelete(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (args.length >= 1 && args[0] instanceof Long) {
            Query query = new Query(
                    Criteria.where("activityId").is(args[0]).and("activityType").is(ActivityType.COMMENT));
            Update update = new Update().set("is_deleted", true);
            mongoTemplate.updateFirst(query, update, "activities");
        }
    }
}

