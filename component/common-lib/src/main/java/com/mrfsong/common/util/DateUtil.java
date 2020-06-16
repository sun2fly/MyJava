package com.mrfsong.common.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 *
 * </p>
 *
 * @Author: Felix
 * @Created: 2020/06/11 21:36
 */
public class DateUtil {

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



    public static String formatTs(Timestamp tsDate, DateTimeFormatter dateTimeFormatter) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(tsDate.getTime()), ZoneId.systemDefault());
        return dateTime.format(dateTimeFormatter);
    }

    public static Timestamp parse2Ts(String dateVar , DateTimeFormatter dateTimeFormatter) {
        LocalDateTime dateTime = LocalDateTime.parse(dateVar, dateTimeFormatter);
        long epochMilli = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return new Timestamp(epochMilli);
    }


    public static LocalDateTime parseTs2DateTime(Timestamp tsDate){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(tsDate.getTime()), ZoneId.systemDefault());
    }





}
