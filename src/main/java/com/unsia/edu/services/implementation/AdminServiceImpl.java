package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.Admin;
import com.unsia.edu.repositories.AdminRepository;
import com.unsia.edu.services.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public Admin createAdmin(Admin admin) {
        Admin existAdmin = this.getAdminByEmail(admin.getEmail());
        if(existAdmin != null) throw new DuplicateKeyException("Email already exist");

        return adminRepository.save(admin);
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return null;
    }
}
