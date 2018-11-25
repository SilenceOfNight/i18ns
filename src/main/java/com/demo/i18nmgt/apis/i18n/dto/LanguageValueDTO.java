package com.demo.i18nmgt.apis.i18n.dto;

import lombok.Data;

/**
 * LanguageValueDTO
 *
 * @author Z
 * @date 2018/11/1
 */
@Data
public class LanguageValueDTO {
    private String language;

    private String value;

    private String createAt;

    private String modifyAt;

    private String verifyAt;
}
