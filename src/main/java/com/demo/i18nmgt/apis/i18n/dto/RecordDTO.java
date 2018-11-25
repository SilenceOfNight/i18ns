package com.demo.i18nmgt.apis.i18n.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * RecordDTO
 *
 * @author Z
 * @date 2018/11/9
 */
@Data
public class RecordDTO {
    private String id;

    private String key;

    private List<LanguageValueDTO> values;

    private String description;

    private String namespaceID;

    private String createAt;

    private String modifyAt;
}
