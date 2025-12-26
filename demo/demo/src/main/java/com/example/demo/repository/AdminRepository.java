package com.example.demo.repository;

import com.example.demo.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    // ✅ 使用 SQL 校验管理员登录
    @Query(value = "SELECT * FROM admin WHERE Username = :username AND Password = :password", nativeQuery = true)
    Admin login(@Param("username") String username, @Param("password") String password);
}