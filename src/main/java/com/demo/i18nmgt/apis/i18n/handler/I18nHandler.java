package com.demo.i18nmgt.apis.i18n.handler;

import com.demo.i18nmgt.apis.common.constants.RespCode;
import com.demo.i18nmgt.apis.common.dto.RespDTO;
import com.demo.i18nmgt.apis.i18n.dto.*;
import com.demo.i18nmgt.apis.i18n.mapper.I18nMapper;
import com.demo.i18nmgt.exception.InternalException;
import com.demo.i18nmgt.repository.i18n.NamespaceRepository;
import com.demo.i18nmgt.repository.i18n.RecordRepository;
import com.demo.i18nmgt.repository.i18n.model.Language;
import com.demo.i18nmgt.repository.i18n.model.LanguageValue;
import com.demo.i18nmgt.repository.i18n.model.Namespace;
import com.demo.i18nmgt.repository.i18n.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * I18nHandler
 *
 * @author Z
 * @date 2018/10/31
 */
@Component
public class I18nHandler {

    @Autowired
    private NamespaceRepository namespaceRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private I18nMapper i18nMapper;

    public Mono<ServerResponse> queryNamespaces(ServerRequest request) {
        Flux<NamespaceDTO> namespaces = this.namespaceRepository.findAll().map(this.i18nMapper::toNamespace);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(namespaces, NamespaceDTO.class);
    }

    public Mono<ServerResponse> queryNamespace(ServerRequest request) {
        return this.namespaceRepository.findById(request.pathVariable("id"))
                .map(this.i18nMapper::toNamespace)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(data)))
                .switchIfEmpty(ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(RespDTO.of(RespCode.NAMESPACE_DOES_NOT_EXIST))));

    }

    public Mono<ServerResponse> createNamespace(ServerRequest request) {
        return request.bodyToMono(NamespaceCreateDTO.class)
                .switchIfEmpty(Mono.error(new InternalException(RespCode.EMPTY_PARAMETER)))
                .map(dto -> {
                    Namespace model = this.i18nMapper.fromCreateNamespace(dto);
                    model.setCreateAt(new Date());
                    return model;
                })
                .flatMap(this.namespaceRepository::insert)
                .map(this.i18nMapper::toNamespace)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(data)))
                .onErrorResume(error -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(RespDTO.of(error))));
    }

    public Mono<ServerResponse> deleteNamespace(ServerRequest request) {
        String namespaceID = request.pathVariable("id");
        Namespace namespace = Namespace.builder().id(namespaceID).build();
        Record record = Record.builder().namespace(namespace).build();
        Example<Record> example = Example.of(record);
        return this.recordRepository.findAll(example)
                .flatMap(this.recordRepository::delete)
                .then(this.namespaceRepository.findById(namespaceID)
                        .flatMap(model -> this.namespaceRepository.delete(model).then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build())))
                .switchIfEmpty(ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(RespDTO.of(RespCode.NAMESPACE_DOES_NOT_EXIST))));
    }

    public Mono<ServerResponse> queryRecords(ServerRequest request) {
        Namespace namespace = Namespace.builder().id(request.pathVariable("namespaceID")).build();
        Record record = Record.builder().namespace(namespace).build();
        Example<Record> example = Example.of(record);
        Flux<RecordDTO> records = this.recordRepository.findAll(example).map(this.i18nMapper::toRecord);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(records, RecordDTO.class);
    }

    public Mono<ServerResponse> queryRecord(ServerRequest request) {
        Namespace namespace = Namespace.builder().id(request.pathVariable("namespaceID")).build();
        Record record = Record.builder().namespace(namespace).id(request.pathVariable("id")).build();
        Example<Record> example = Example.of(record);

        return this.recordRepository.findOne(example)
                .map(this.i18nMapper::toRecord)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(data)))
                .switchIfEmpty(ServerResponse.badRequest().body(BodyInserters.fromObject(RespDTO.of(RespCode.RECORD_DOES_NOT_EXIST))));
    }

    public Mono<ServerResponse> createRecord(ServerRequest request) {
        return namespaceRepository.findById(request.pathVariable("namespaceID"))
                .switchIfEmpty(Mono.error(new InternalException(RespCode.NAMESPACE_DOES_NOT_EXIST)))
                .zipWith(request.bodyToMono(RecordOperateDTO.class).map(this.i18nMapper::fromOperateRecord), (namespace, record) -> {
                    List<Language> languages = namespace.getLanguages();
                    List<LanguageValue> languageValues = record.getValues();
                    for (LanguageValue languageValue : languageValues) {
                        String language = languageValue.getLanguage();
                        if (!existLanguage(language, languages)) {
                            throw new InternalException(RespCode.LANGUAGE_DOES_NOT_BE_SUPPORTED);
                        }
                        languageValue.setCreateAt(new Date());
                    }
                    record.setNamespace(namespace);
                    record.setCreateAt(new Date());

                    return record;
                })
                .flatMap(this.recordRepository::insert)
                .map(this.i18nMapper::toRecord)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(data)))
                .onErrorResume(error -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(RespDTO.of(error))));
    }

    private boolean existLanguage(String language, List<Language> languages) {
        for (Language curLanguage : languages) {
            if (language.equals(curLanguage.getValue())) {
                return true;
            }
        }
        return false;
    }

    public Mono<ServerResponse> modifyRecord(ServerRequest request) {
        Namespace namespace = Namespace.builder().id(request.pathVariable("namespaceID")).build();
        Record record = Record.builder().namespace(namespace).id(request.pathVariable("id")).build();
        Example<Record> example = Example.of(record);

        return this.recordRepository.findOne(example)
                .switchIfEmpty(Mono.error(new InternalException(RespCode.RECORD_DOES_NOT_EXIST)))
                .zipWith(request.bodyToMono(RecordOperateDTO.class).map(this.i18nMapper::fromOperateRecord), this::mergeRecord)
                .flatMap(this.recordRepository::save)
                .map(this.i18nMapper::toRecord)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(data)))
                .onErrorResume(error -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(RespDTO.of(error))));
    }

    public Mono<ServerResponse> deleteRecord(ServerRequest request) {
        Namespace namespace = Namespace.builder().id(request.pathVariable("namespaceID")).build();
        Record record = Record.builder().namespace(namespace).id(request.pathVariable("id")).build();
        Example<Record> example = Example.of(record);

        return this.recordRepository.findOne(example)
                .flatMap(model -> this.recordRepository.delete(model).then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).build()))
                .switchIfEmpty(ServerResponse.badRequest().body(BodyInserters.fromObject(RespDTO.of(RespCode.RECORD_DOES_NOT_EXIST))));
    }

    public Mono<ServerResponse> deleteRecords(ServerRequest request) {
        String namespaceID = request.pathVariable("namespaceID");
        request.bodyToMono(new ParameterizedTypeReference<List<String>>() {
        }).subscribe(recordIds -> {
            System.out.println(recordIds);
        });
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).build();
    }

    public Mono<ServerResponse> queryLanguages(ServerRequest request) {
        String namespaceID = request.pathVariable("namespaceID");
        return this.namespaceRepository.findById(namespaceID)
                .switchIfEmpty(Mono.error(new InternalException(RespCode.NAMESPACE_DOES_NOT_EXIST)))
                .map(Namespace::getLanguages)
                .map(i18nMapper::toLanguages)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(data)))
                .onErrorResume(error -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(RespDTO.of(error))));
    }

    public Mono<ServerResponse> createLanguage(ServerRequest request) {
        String namespaceID = request.pathVariable("namespaceID");
        return this.namespaceRepository.findById(namespaceID)
                .switchIfEmpty(Mono.error(new InternalException(RespCode.NAMESPACE_DOES_NOT_EXIST)))
                .zipWith(request.bodyToMono(LanguageDTO.class).map(this.i18nMapper::fromLanguage), (namespace, language) -> {
                    List<Language> languages = namespace.getLanguages();
                    if (languages == null) {
                        languages = new ArrayList<>();
                        namespace.setLanguages(languages);
                    } else {
                        Language preLanguage = existLanguage(language.getValue(), namespace);
                        if (preLanguage != null) {
                            throw new InternalException(RespCode.LANGUAGE_DOES_EXIST);
                        }
                    }
                    languages.add(language);
                    return namespace;
                })
                .flatMap(this.namespaceRepository::save)
                .map(this.i18nMapper::toNamespace)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(data)))
                .onErrorResume(error -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(RespDTO.of(error))));
    }

    public Mono<ServerResponse> modifyLanguages(ServerRequest request) {
        String namespaceID = request.pathVariable("namespaceID");
        String language = request.pathVariable("language");
        return this.namespaceRepository.findById(namespaceID)
                .switchIfEmpty(Mono.error(new InternalException(RespCode.NAMESPACE_DOES_NOT_EXIST)))
                .zipWith(request.bodyToMono(LanguageDTO.class).map(this.i18nMapper::fromLanguage), (namespace, reqLanguage) -> {
                    Language preLanguage = existLanguage(language, namespace);
                    if (preLanguage == null) {
                        throw new InternalException(RespCode.LANGUAGE_DOES_NOT_BE_SUPPORTED);
                    }
                    if (reqLanguage.getName() != null) {
                        preLanguage.setName(reqLanguage.getName());
                    }
                    if (reqLanguage.getValue() != null) {

                        preLanguage.setValue(reqLanguage.getValue());
                    }
                    return namespace;
                })
                .flatMap(this.namespaceRepository::save)
                .map(this.i18nMapper::toNamespace)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(data)))
                .onErrorResume(error -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(RespDTO.of(error))));
    }

    public Mono<ServerResponse> deleteLanguage(ServerRequest request) {
        String namespaceID = request.pathVariable("namespaceID");
        String language = request.pathVariable("language");
        return this.namespaceRepository.findById(namespaceID)
                .switchIfEmpty(Mono.error(new InternalException(RespCode.NAMESPACE_DOES_NOT_EXIST)))
                .map(namespace -> {
                    Language preLanguage = existLanguage(language, namespace);
                    if (preLanguage == null) {
                        throw new InternalException(RespCode.LANGUAGE_DOES_NOT_BE_SUPPORTED);
                    }
                    List<Language> languages = namespace.getLanguages();
                    languages.remove(preLanguage);
                    return namespace;
                })
                .flatMap(this.namespaceRepository::save)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(data)))
                .onErrorResume(error -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(RespDTO.of(error))));
    }

    private Record mergeRecord(Record previous, Record next) {
        Namespace namespace = previous.getNamespace();
        List<Language> languages = namespace.getLanguages();
        if (languages == null || languages.isEmpty()) {
            throw new InternalException(RespCode.NO_LANGUAGE);
        }

        boolean hasChange = false;

        if (next.getKey() != null && !next.getKey().equals(previous.getKey())) {
            hasChange = true;
            previous.setKey(next.getKey());
        }
        if (next.getDescription() != null && !next.getDescription().equals(previous.getDescription())) {
            hasChange = true;
            previous.setDescription(next.getDescription());
        }

        List<LanguageValue> nextValues = next.getValues();
        if (nextValues == null || nextValues.isEmpty()) {
            if (hasChange) {
                previous.setModifyAt(new Date());
            }
            return previous;
        }

        Language language = null;
        for (LanguageValue nextValue : nextValues) {
            language = existLanguage(nextValue.getLanguage(), namespace);
            if (language == null) {
                break;
            }
        }
        if (language == null) {
            throw new InternalException(RespCode.LANGUAGE_DOES_NOT_BE_SUPPORTED);
        }

        List<LanguageValue> preValues = previous.getValues();
        if (preValues == null || preValues.isEmpty()) {
            previous.setValues(nextValues);
            previous.setModifyAt(new Date());
            return previous;
        }

        for (LanguageValue nextValue : nextValues) {
            LanguageValue preValue = existLanguageValue(nextValue.getLanguage(), preValues);
            if (preValue == null) {
                hasChange = true;
                nextValue.setCreateAt(new Date());
                preValues.add(nextValue);
            } else {
                if (!nextValue.getValue().equals(preValue.getValue())) {
                    hasChange = true;
                    preValue.setValue(nextValue.getValue());
                    preValue.setModifyAt(new Date());
                }
            }
        }

        if (hasChange) {
            previous.setModifyAt(new Date());
        }
        return previous;
    }

    private Language existLanguage(String language, Namespace namespace) {
        if (language == null || namespace == null) {
            return null;
        }
        List<Language> languages = namespace.getLanguages();
        if (languages != null && !languages.isEmpty()) {
            for (Language curLanguage : languages) {
                if (language.equals(curLanguage.getValue())) {
                    return curLanguage;
                }
            }
        }
        return null;
    }

    private LanguageValue existLanguageValue(String language, List<LanguageValue> languageValues) {
        if (language == null || languageValues == null || languageValues.isEmpty()) {
            return null;
        }
        for (LanguageValue languageValue : languageValues) {
            if (language.equals(languageValue.getLanguage())) {
                return languageValue;
            }
        }
        return null;
    }

    public Mono<ServerResponse> verifyRecord(ServerRequest request) {
        String language = request.pathVariable("language");
        Namespace namespace = Namespace.builder().id(request.pathVariable("namespaceID")).build();
        Record record = Record.builder().namespace(namespace).id(request.pathVariable("recordID")).build();
        Example<Record> example = Example.of(record);

        return this.recordRepository.findOne(example)
                .switchIfEmpty(Mono.error(new InternalException(RespCode.RECORD_DOES_NOT_EXIST)))
                .map(model -> {
                    List<LanguageValue> languageValues = model.getValues();
                    LanguageValue languageValue = existLanguageValue(language, languageValues);
                    if (languageValue == null) {
                        throw new InternalException(RespCode.LANGUAGE_DOES_NOT_BE_SUPPORTED);
                    }
                    languageValue.setVerifyAt(new Date());
                    return model;
                })
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(data)))
                .onErrorResume(error -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(RespDTO.of(error))));
    }

    public Mono<ServerResponse> importRecords(ServerRequest request) {
//        String namespaceID = request.pathVariable("namespaceID");
//        Flux<RecordImportDTO> flux = request.bodyToFlux(RecordImportDTO.class);
        return ServerResponse.ok().build();
    }


}
