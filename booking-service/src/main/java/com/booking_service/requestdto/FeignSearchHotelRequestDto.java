package com.booking_service.requestdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class FeignSearchHotelRequestDto {
    private String  cityName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date checkInDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date checkOutDate;
}
