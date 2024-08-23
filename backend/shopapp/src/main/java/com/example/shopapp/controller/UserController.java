package com.example.shopapp.controller;

import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.dto.UserLoginDTO;
import com.example.shopapp.entity.User;
import com.example.shopapp.response.LoginResponse;
import com.example.shopapp.service.UserService;
import com.example.shopapp.component.LocalizationUtil;
import com.example.shopapp.util.MessageKeys;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    private final LocalizationUtil localizationUtils;
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody @Valid UserDTO userDTO){
        return new ResponseEntity<>(
                userService.createUser(userDTO),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid UserLoginDTO userLoginDTO){
        userLoginDTO.setRoleId(userLoginDTO.getRoleId() == null ? 1L : userLoginDTO.getRoleId());
        String token = userService.login(userLoginDTO);
        return ResponseEntity.ok(LoginResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                        .token(token)
                        .build());
    }
}
