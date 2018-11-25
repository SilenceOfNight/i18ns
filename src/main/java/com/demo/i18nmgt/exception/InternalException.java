package com.demo.i18nmgt.exception;

import com.demo.i18nmgt.apis.common.constants.RespCode;
import com.demo.i18nmgt.apis.common.dto.RespDTO;

/**
 * InternalException
 *
 * @author Z
 * @date 2018/10/21
 */
public class InternalException extends RuntimeException {
    private RespCode responseCode;

    private Object data;

    public InternalException(RespCode responseCode) {
        super(responseCode.getDescription());
        this.responseCode = responseCode;
    }

    public InternalException(RespCode responseCode, Object data) {
        super(responseCode.getDescription());
        this.responseCode = responseCode;
        this.data = data;
    }

    public RespDTO toResponse() {
        return RespDTO.of(this.responseCode, this.data);
    }
}
