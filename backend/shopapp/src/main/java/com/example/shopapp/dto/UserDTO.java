package com.example.shopapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String fullname;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "Password is required")
    private String password;

    private String retypePassword;

    private Date dateOfBirth;

    private int facebookAccountId;

    private int googleAccountId;

    @NotNull(message = "Role Id is required")
    private Long roleId;
}
