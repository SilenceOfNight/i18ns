package com.demo.i18nmgt.repository.i18n.model;

import lombok.Data;

import java.util.Date;

/**
 * LanguageValue
 *
 * @author Z
 * @date 2018/11/1
 */
@Data
public class LanguageValue {
    private String language;

    private String value;

    private Date createAt;

    private Date modifyAt;

    private Date verifyAt;
}
