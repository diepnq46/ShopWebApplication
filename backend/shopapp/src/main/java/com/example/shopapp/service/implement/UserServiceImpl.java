package com.example.shopapp.service.implement;

import com.example.shopapp.component.JwtTokenUtil;
import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.dto.UserLoginDTO;
import com.example.shopapp.entity.Role;
import com.example.shopapp.entity.User;
import com.example.shopapp.exception.InvalidException;
import com.example.shopapp.exception.PermissionDenyException;
import com.example.shopapp.exception.ResourceNotFoundException;
import com.example.shopapp.mapper.UserMapper;
import com.example.shopapp.repository.RoleRepository;
import com.example.shopapp.repository.UserRepository;
import com.example.shopapp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    @Transactional
    public User createUser(UserDTO userDTO) {
        // Kiểm tra xem số điện thoại đã tồn tại chưa
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new InvalidException("Phone number is already exist");
        }

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() ->new ResourceNotFoundException("Role", "id", userDTO.getRoleId()));

        if (role.getName().equals("admin")){
            throw new PermissionDenyException("You cannot register an admin account");
        }

        // Chuyển DTO qua JPA Entity
        User user = UserMapper.MAPPER.mapToUser(userDTO);

        user.setRole(role);

        // Kiểm tra nếu có accountId, không yêu cầu password
        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
           if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
               throw new InvalidException("Invalid retype password");
           }

            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encodedPassword);
        }

        // Lưu vào db
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByPhoneNumber(userLoginDTO.getPhoneNumber())
                .orElseThrow(() -> new InvalidException("Invalid username/password"));

        if(!Objects.equals(user.getRole().getId(), userLoginDTO.getRoleId())) {
            throw new InvalidException("Invalid username/password");
        }

        if(user.getFacebookAccountId() == 0 && user.getGoogleAccountId() == 0){
            if(!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())){
                throw new BadCredentialsException("Wrong phone number or password");
            }

        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userLoginDTO.getPhoneNumber(),
                userLoginDTO.getPassword(),
                user.getAuthorities()
        );

        // authenticate with Java spring
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtil.generateToken(user);
    }
}
