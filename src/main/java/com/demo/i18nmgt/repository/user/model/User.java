package com.demo.i18nmgt.repository.user.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * User
 *
 * @author Z
 * @date 2018/10/20
 */
@Data
@Document
public class User {
    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    @Size(min = 4, max = 32)
    private String account;

    @NotBlank
    @Size(min = 6, max = 32)
    private String password;

    @NotBlank
    @Size(min = 1, max = 128)
    private String name;

    @NotNull
    private Date createAt = new Date();

    private Date modifyAt;
}
