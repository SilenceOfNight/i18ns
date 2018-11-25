package com.demo.i18nmgt.apis.i18n.dto;

import lombok.Data;

import java.util.List;

/**
 * NamespaceCreateDTO
 *
 * @author Z
 * @date 2018/11/1
 */
@Data
public class NamespaceCreateDTO {
    private String name;

    private List<LanguageDTO> languages;
}
