package com.demo.i18nmgt.apis.common.dto;

import com.demo.i18nmgt.apis.common.constants.RespCode;
import com.demo.i18nmgt.exception.InternalException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * RespDTO
 *
 * @author Z
 * @date 2018/10/21
 */
@Data
@Slf4j
public class RespDTO<T> {
    public static final RespDTO SUCCESS = RespDTO.of(RespCode.SUCCESS);

    public static final RespDTO FAILURE = RespDTO.of(RespCode.FAILURE);

    private String code;

    private String description;

    private T data;

    public static RespDTO<?> of(String code, String description, Object data) {
        RespDTO<Object> dto = new RespDTO<>();
        dto.setCode(code);
        dto.setDescription(description);
        dto.setData(data);
        return dto;
    }

    public static RespDTO<?> of(RespCode responseCode, Object data) {
        return RespDTO.of(responseCode.getCode(), responseCode.getDescription(), data);
    }

    public static RespDTO<?> of(RespCode responseCode) {
        return RespDTO.of(responseCode, null);
    }

    public static RespDTO<?> of(Throwable throwable) {
        if (throwable instanceof InternalException) {
            InternalException exception = (InternalException) throwable;
            
            return exception.toResponse();
        }
        return RespDTO.FAILURE;
    }

    public static RespDTO<?> success(Object data) {
        return RespDTO.of(RespCode.SUCCESS, data);
    }
}
