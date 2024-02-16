package com.hh.mirishop.activity.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "user-service", url = "${external.user-service.url}")
public interface UserFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/internal/members/{memberNumber}")
    ResponseEntity<Boolean> findMemberByNumber(@PathVariable("memberNumber") Long memberNumber);
}
