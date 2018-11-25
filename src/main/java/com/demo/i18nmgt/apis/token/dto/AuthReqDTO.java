package com.demo.i18nmgt.apis.token.dto;

import lombok.Data;

/**
 * AuthReqDTO
 *
 * @author Z
 * @date 2018/10/28
 */
@Data
public class AuthReqDTO {
    private String account;

    private String password;
}
