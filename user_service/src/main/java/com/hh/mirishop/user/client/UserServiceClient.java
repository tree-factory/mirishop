package com.hh.mirishop.user.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service", url = "${feign.user-service.url")
public interface UserServiceClient {
}
