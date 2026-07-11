package com.booking_service.feignclient;

import com.commonlibrary.common_library.common.enums.FacilityType;
import lombok.Data;

import java.util.UUID;
@Data
public class FeignFacilityResponseDto {
    private UUID faciltiyId;
    private FacilityType facilityType;
}
