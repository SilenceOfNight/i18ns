package com.demo.i18nmgt.apis.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * UserDTO
 *
 * @author Z
 * @date 2018/10/28
 */
@Data
public class UserDTO {
    private String id;

    private String account;

    private String name;

    private String createAt;

    private String modifyAt;
}
