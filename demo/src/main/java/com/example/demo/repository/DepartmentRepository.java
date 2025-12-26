package com.example.demo.repository;

import com.example.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    // ✅ 修改：使用 SQL 根据名称查单位
    // 对应数据库字段: DeptName
    @Query(value = "SELECT * FROM department WHERE DeptName = :deptName", nativeQuery = true)
    Department findByDeptName(@Param("deptName") String deptName);
}