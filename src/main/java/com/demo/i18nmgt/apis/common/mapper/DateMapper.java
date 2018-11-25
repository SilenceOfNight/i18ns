package com.demo.i18nmgt.apis.common.mapper;

import com.demo.i18nmgt.apis.common.constants.RespCode;
import com.demo.i18nmgt.exception.InternalException;
import org.mapstruct.Mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateMapper
 *
 * @author Z
 * @date 2018/11/16
 */
@Mapper(componentModel = "spring")
public class DateMapper {

    public String asString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }

    public Date asDate(String date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new InternalException(RespCode.FAILURE, "Failed to parse date.");
        }
    }
}
