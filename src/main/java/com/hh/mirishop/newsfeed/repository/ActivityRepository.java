package com.hh.mirishop.newsfeed.repository;

import com.hh.mirishop.newsfeed.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends MongoRepository<Activity, Long> {

    Page<Activity> findAllByMemberNumber(List<Long> following, PageRequest of);
}
