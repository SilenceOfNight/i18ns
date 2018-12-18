package com.demo.i18nmgt.apis.token.handler;

import com.demo.i18nmgt.apis.common.constants.RespCode;
import com.demo.i18nmgt.apis.common.dto.RespDTO;
import com.demo.i18nmgt.apis.common.utils.ConvertorUtils;
import com.demo.i18nmgt.apis.token.dto.AuthReqDTO;
import com.demo.i18nmgt.apis.token.dto.TokenRespDTO;
import com.demo.i18nmgt.apis.user.dto.UserDTO;
import com.demo.i18nmgt.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

/**
 * TokenHandler
 *
 * @author Z
 * @date 2018/10/28
 */
@Component
public class TokenHandler {
    private UserRepository userRepository;

    @Autowired
    public TokenHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<ServerResponse> queryToken(ServerRequest request) {
        String account = request.pathVariable("token");

        return request.bodyToMono(AuthReqDTO.class)
                .flatMap(auth -> this.userRepository.findUserByAccount(auth.getAccount()))
                .flatMap(user -> ServerResponse.ok().body(BodyInserters.fromObject(RespDTO.of(RespCode.SUCCESS, ConvertorUtils.convert(user, UserDTO.class)))))
                .switchIfEmpty(ServerResponse.badRequest().body(BodyInserters.fromObject(RespDTO.of(RespCode.USER_DOES_NOT_EXIST))));
    }


    public <T extends ServerResponse> Mono<ServerResponse> createToken(ServerRequest request) {
        return request.bodyToMono(AuthReqDTO.class)
                .flatMap(auth -> this.userRepository.findUserByAccount(auth.getAccount())
                        .flatMap(user -> {
                            if (auth.getPassword().equals(user.getPassword())) {
                                TokenRespDTO token = TokenRespDTO.builder()
                                        .token(UUID.randomUUID().toString())
                                        .createAt(new Date())
                                        .user(ConvertorUtils.convert(user, UserDTO.class))
                                        .build();
                                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(RespDTO.of(RespCode.SUCCESS, token)));
                            }

                            return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(RespDTO.of(RespCode.AUTHENTICATE_FAILURE)));
                        })
                        .switchIfEmpty(ServerResponse.notFound().build())
                );

    }

    public Mono<ServerResponse> deleteToken(ServerRequest request) {
        return null;
    }
}
