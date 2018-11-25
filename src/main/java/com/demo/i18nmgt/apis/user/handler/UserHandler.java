package com.demo.i18nmgt.apis.user.handler;

import com.demo.i18nmgt.apis.common.constants.RespCode;
import com.demo.i18nmgt.apis.common.dto.RespDTO;
import com.demo.i18nmgt.apis.user.dto.UserDTO;
import com.demo.i18nmgt.apis.user.dto.UserOperateDTO;
import com.demo.i18nmgt.apis.user.mapper.UserMapper;
import com.demo.i18nmgt.repository.user.UserRepository;
import com.demo.i18nmgt.repository.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * UserHandler
 *
 * @author Z
 * @date 2018/10/20
 */
@Component
public class UserHandler {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper userMapper;

    public Mono<ServerResponse> queryUsers(ServerRequest request) {
        Flux<UserDTO> users = this.repository.findAll().map(this.userMapper::userModel2DTO);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(users, UserDTO.class);
    }

    public Mono<ServerResponse> queryUser(ServerRequest request) {
        String account = request.pathVariable("account");
        return this.repository.findUserByAccount(account)
                .map(this.userMapper::userModel2DTO)
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(RespDTO.success(dto))))
                .switchIfEmpty(ServerResponse.badRequest().body(BodyInserters.fromObject(RespDTO.of(RespCode.USER_NOT_EXIST))));
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UserOperateDTO.class)
                .map(dto -> {
                    User model = this.userMapper.fromOperateUser(dto);
                    model.setCreateAt(new Date());
                    return model;
                })
                .flatMap(this.repository::insert)
                .map(this.userMapper::userModel2DTO)
                .flatMap(user -> ServerResponse.created(URI.create("/api/user/" + user.getAccount())).contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(RespDTO.success(user))))
                .onErrorResume(error -> ServerResponse.badRequest().body(BodyInserters.fromObject(RespDTO.of(error))));
    }

    public Mono<ServerResponse> modifyUser(ServerRequest request) {
        String account = request.pathVariable("account");

        return this.repository.findUserByAccount(account)
                .zipWith(request.bodyToMono(UserOperateDTO.class), (model, dto) -> {
                    model.setName(dto.getName());
                    model.setPassword(dto.getPassword());
                    model.setModifyAt(new Date());
                    return model;
                })
                .flatMap(this.repository::save)
                .map(this.userMapper::userModel2DTO)
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(dto)))
                .switchIfEmpty(ServerResponse.badRequest().body(fromObject(RespDTO.of(RespCode.USER_NOT_EXIST))));
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        String account = request.pathVariable("account");

        return this.repository.findUserByAccount(account)
                .flatMap(model -> this.repository.delete(model).then(ServerResponse.ok().body(fromObject(RespDTO.SUCCESS))))
                .switchIfEmpty(ServerResponse.badRequest().body(fromObject(RespDTO.of(RespCode.USER_NOT_EXIST))));
    }
}
