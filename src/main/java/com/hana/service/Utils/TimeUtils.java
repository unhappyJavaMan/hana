package com.hana.service.Utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimeUtils {
    public static LocalDateTime getNowUTCLocalDateTime(){
        return ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime();
    }
    public static String localDateTimeToTimeStamp(LocalDateTime localDateTime){
        if (localDateTime != null){
            return String.valueOf(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
        }else {
            return null;
        }
    }
}
