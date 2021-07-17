package com.shred.springmvc.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 自定义类型转换器
 */
public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String s) {
        LocalDateTime parse = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
    }
}
