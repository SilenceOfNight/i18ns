package com.demo.i18nmgt.repository.user;

import com.demo.i18nmgt.repository.user.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * UserRepository
 *
 * @author Z
 * @date 2018/10/20
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    /**
     * 根据帐号查询用户信息
     * @param account 用户帐号
     * @return 用户信息
     */
    Mono<User> findUserByAccount(String account);
}
