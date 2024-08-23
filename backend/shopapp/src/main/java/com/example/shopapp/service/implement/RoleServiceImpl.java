package com.example.shopapp.service.implement;

import com.example.shopapp.entity.Role;
import com.example.shopapp.repository.RoleRepository;
import com.example.shopapp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
