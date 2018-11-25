package com.demo.i18nmgt;

import com.demo.i18nmgt.apis.i18n.handler.I18nHandler;
import com.demo.i18nmgt.apis.token.handler.TokenHandler;
import com.demo.i18nmgt.apis.user.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * RouterConfiguration
 *
 * @author Z
 * @date 2018/10/20
 */
@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/apis/v1/users").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::queryUsers)
                .andRoute(RequestPredicates.GET("/apis/v1/users/{account}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::queryUser)
                .andRoute(RequestPredicates.POST("/apis/v1/users").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::createUser)
                .andRoute(RequestPredicates.PUT("/apis/v1/users/{account}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::modifyUser)
                .andRoute(RequestPredicates.DELETE("/apis/v1/users/{account}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::deleteUser);
    }

    @Bean
    public RouterFunction<ServerResponse> tokenRouterFunction(TokenHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.POST("/apis/v1/tokens").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::createToken)
                .andRoute(RequestPredicates.DELETE("/apis/v1/tokens/{token}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::deleteToken)
                .andRoute(RequestPredicates.GET("/apis/v1/tokens/{token}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::queryToken);
    }

    @Bean
    public RouterFunction<ServerResponse> i18nRouterFunction(I18nHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/apis/v1/i18n/namespaces").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::queryNamespaces)
                .andRoute(RequestPredicates.GET("/apis/v1/i18n/namespaces/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::queryNamespace)
                .andRoute(RequestPredicates.POST("/apis/v1/i18n/namespaces").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::createNamespace)
                .andRoute(RequestPredicates.DELETE("/apis/v1/i18n/namespaces/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::deleteNamespace)
                .andRoute(RequestPredicates.GET("/apis/v1/i18n/namespaces/{namespaceID}/languages").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::queryLanguages)
                .andRoute(RequestPredicates.POST("/apis/v1/i18n/namespaces/{namespaceID}/languages").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::createLanguage)
                .andRoute(RequestPredicates.PUT("/apis/v1/i18n/namespaces/{namespaceID}/languages/{language}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::modifyLanguages)
                .andRoute(RequestPredicates.DELETE("/apis/v1/i18n/namespaces/{namespaceID}/languages/{language}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::deleteLanguages)
                .andRoute(RequestPredicates.GET("/apis/v1/i18n/namespaces/{namespaceID}/records").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::queryRecords)
                .andRoute(RequestPredicates.GET("/apis/v1/i18n/namespaces/{namespaceID}/records/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::queryRecord)
                .andRoute(RequestPredicates.POST("/apis/v1/i18n/namespaces/{namespaceID}/records").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::createRecord)
                .andRoute(RequestPredicates.PUT("/apis/v1/i18n/namespaces/{namespaceID}/records/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::modifyRecord)
                .andRoute(RequestPredicates.DELETE("/apis/v1/i18n/namespaces/{namespaceID}/records/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::deleteRecord)
                .andRoute(RequestPredicates.PUT("/apis/v1/i18n/namespaces/{namespaceID}/records/{recordID}/languages/{language}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)), handler::verifyRecord);

    }
}
