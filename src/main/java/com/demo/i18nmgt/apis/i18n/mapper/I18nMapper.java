package com.demo.i18nmgt.apis.i18n.mapper;

import com.demo.i18nmgt.apis.common.mapper.DateMapper;
import com.demo.i18nmgt.apis.i18n.dto.*;
import com.demo.i18nmgt.repository.i18n.model.Language;
import com.demo.i18nmgt.repository.i18n.model.Namespace;
import com.demo.i18nmgt.repository.i18n.model.Record;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * I18nMapper
 *
 * @author Z
 * @date 2018/11/9
 */
@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface I18nMapper {

    /**
     * Convert from the model to DTO of Record
     *
     * @param model the converting model of Record
     * @return the converted DTO of Record
     */
    @Mappings({@Mapping(source = "namespace.id", target = "namespaceID")})
    RecordDTO toRecord(Record model);

    /**
     * Convert from the DTO to model of Record
     *
     * @param dto the converting DTO of Record
     * @return the converted model of Record
     */
    Record fromOperateRecord(RecordOperateDTO dto);

    /**
     * Convert from the model to DTO of Namespace
     *
     * @param model the converting model of Namespace
     * @return the converted DTO of Namespace
     */
    NamespaceDTO toNamespace(Namespace model);

    /**
     * Convert from the DTO to model of Namespace
     *
     * @param dto the converting DTO of Namespace
     * @return the converted model of Namespace
     */
    Namespace fromCreateNamespace(NamespaceCreateDTO dto);

    /**
     * Convert from the model to DTO of Language
     *
     * @param model the converting model of Language
     * @return the converted DTO of Language
     */
    LanguageDTO toLanguage(Language model);

    /**
     * Convert from the model to DTO of Languages
     *
     * @param models the converting model list of Language
     * @return the converted DTO list of Language
     */
    List<LanguageDTO> toLanguages(List<Language> models);

    /**
     * Convert from the DTO to model of Language
     *
     * @param dto the converting DTO of Language
     * @return the converted model of Language
     */
    Language fromLanguage(LanguageDTO dto);
}
