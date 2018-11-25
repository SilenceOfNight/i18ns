package com.demo.i18nmgt.apis.i18n.dto;

import com.demo.i18nmgt.repository.i18n.model.Language;
import lombok.Data;

import java.util.List;

/**
 * NamespaceDTO
 *
 * @author Z
 * @date 2018/11/9
 */
@Data
public class NamespaceDTO {
    private String id;

    private String name;

    private List<LanguageDTO> languages;

    private String createAt;

    private String modifyAt;
}
