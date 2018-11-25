package com.demo.i18nmgt.apis.user.mapper;

import com.demo.i18nmgt.apis.user.dto.UserOperateDTO;
import com.demo.i18nmgt.apis.user.dto.UserDTO;
import com.demo.i18nmgt.repository.user.model.User;
import org.mapstruct.Mapper;

/**
 * UserMapper
 *
 * @author Z
 * @date 2018/11/11
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userModel2DTO(User model);

    User fromOperateUser(UserOperateDTO dto);
}
