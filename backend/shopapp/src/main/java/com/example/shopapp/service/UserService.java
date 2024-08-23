package com.example.shopapp.service;

import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.dto.UserLoginDTO;
import com.example.shopapp.entity.User;

public interface UserService {
    User createUser(UserDTO userDTO);

    String login(UserLoginDTO userLoginDTO);
}
