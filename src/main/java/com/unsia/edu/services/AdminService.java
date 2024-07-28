package com.unsia.edu.services;

import com.unsia.edu.entities.Admin;

public interface AdminService {
    Admin createAdmin(Admin admin);
    Admin getAdminByEmail(String email);
    Admin updateAdmin(Admin admin);
}
