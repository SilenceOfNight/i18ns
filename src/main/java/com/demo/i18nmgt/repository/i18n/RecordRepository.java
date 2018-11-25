package com.demo.i18nmgt.repository.i18n;

import com.demo.i18nmgt.repository.i18n.model.Record;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * RecordRepository
 *
 * @author Z
 * @date 2018/11/1
 */
public interface RecordRepository extends ReactiveMongoRepository<Record, String> {
}
