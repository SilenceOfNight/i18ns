package com.demo.i18nmgt.repository.i18n.model;

import com.demo.i18nmgt.repository.CollectionNames;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Record
 *
 * @author Z
 * @date 2018/11/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = CollectionNames.RECORD)
public class Record {
    private String id;

    private String key;

    private List<LanguageValue> values;

    private String description;

    @DBRef
    private Namespace namespace;

    private Date createAt;

    private Date modifyAt;
}
