package com.example.shopapp.mapper;

import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
    UserDTO mapToUserDTO(User user);
    User mapToUser(UserDTO userDTO);
}
