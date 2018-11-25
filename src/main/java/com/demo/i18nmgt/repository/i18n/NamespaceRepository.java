package com.demo.i18nmgt.repository.i18n;

import com.demo.i18nmgt.repository.i18n.model.Namespace;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * NamespaceRepository
 *
 * @author Z
 * @date 2018/11/1
 */
public interface NamespaceRepository extends ReactiveMongoRepository<Namespace, String> {
}
