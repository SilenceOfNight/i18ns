package com.demo.i18nmgt.apis.i18n.dto;

import com.demo.i18nmgt.repository.i18n.model.LanguageValue;
import lombok.Data;

import java.util.List;

/**
 * RecordCreateDTO
 *
 * @author Z
 * @date 2018/11/1
 */
@Data
public class RecordCreateDTO {
    private String key;

    private String description;

    private List<LanguageValueDTO> values;
}
