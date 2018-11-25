package com.demo.i18nmgt.apis.token.dto;

import com.demo.i18nmgt.apis.user.dto.UserDTO;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * TokenRespDTO
 *
 * @author Z
 * @date 2018/10/28
 */
@Data
@Builder
public class TokenRespDTO {
    private String token;

    private UserDTO user;

    private Date createAt;

    private Date expireAt;

}
