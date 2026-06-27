package com.locationservice.locationservice.locations.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "location-service")
public interface LocationFeignClient {
}
