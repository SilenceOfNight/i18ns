package com.demo.i18nmgt.repository.i18n.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Language
 *
 * @author Z
 * @date 2018/11/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Language {
    private String name;

    private String value;
}
