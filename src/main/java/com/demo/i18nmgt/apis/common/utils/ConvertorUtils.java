package com.demo.i18nmgt.apis.common.utils;

import com.demo.i18nmgt.apis.common.constants.RespCode;
import com.demo.i18nmgt.exception.InternalException;
import org.springframework.beans.BeanUtils;

/**
 * ConvertorUtils
 *
 * @author Z
 * @date 2018/10/28
 */
public class ConvertorUtils {
    public static <S, T> T convert(S source, Class<T> descriptor) {
        try {
            T target = descriptor.newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InternalException(RespCode.FAILURE, "Failed to convert data, source: " + source.toString());
        }
    }
}
