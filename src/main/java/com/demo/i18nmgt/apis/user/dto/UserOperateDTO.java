package com.demo.i18nmgt.apis.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * UserOperateDTO
 *
 * @author Z
 * @date 2018/10/28
 */
@Data
public class UserOperateDTO {
    private String account;

    private String password;

    private String name;
}
