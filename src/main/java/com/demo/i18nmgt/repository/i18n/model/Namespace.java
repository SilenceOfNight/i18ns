package com.demo.i18nmgt.repository.i18n.model;

import com.demo.i18nmgt.repository.CollectionNames;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Namespace
 *
 * @author Z
 * @date 2018/11/1
 */
@Data
@Builder
@Document(collection = CollectionNames.NAMESPACE)
@NoArgsConstructor
@AllArgsConstructor
public class Namespace {
    @Id
    private String id;

    private String name;

    private List<Language> languages;

    private Date createAt;

    private Date modifyAt;
}
